package RDFValidation.controller;

import helper.DynaTree;
import helper.FileHelper;
import helper.FileMeta;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import RDFValidation.Spin;
import RDFValidation.ValidationEnvironment;

@Controller
@SessionAttributes( "validationEnvironment" )
@RequestMapping( value = "/owl2" )
public class OWL2Controller
{
	final String nonLiteralValueConstraintsPath = "/resources/rdfGraphs/DSP/Non-LiteralValueConstraints";
	final String dspFileUploadPath = "/resources/uploaded_files/";

	/* variable for multiple file upload */
	LinkedList<FileMeta> files = new LinkedList<FileMeta>();

	FileMeta fileMeta = null;

	// tab 0
	@RequestMapping( method = RequestMethod.GET )
	public ModelAndView landing( @RequestParam( value = "sessionid", required = false ) final String sessionId, final HttpServletResponse response )
	{
		ModelAndView model = new ModelAndView( "owl2", "link", "owl2" );

		if ( sessionId != null && sessionId.equals( "0" ) )
			response.setHeader( "SESSION_INVALID", "yes" );

		// initialize session variable
		model.addObject( "validationEnvironment", new ValidationEnvironment() );

		return model;
	}

	/**
	 * 
	 * @param sessionId
	 * @param response
	 * @return
	 */
	@RequestMapping( value = "/resource_structure", method = RequestMethod.GET )
	public @ResponseBody
	List<DynaTree> directoryStructure( HttpServletRequest request, HttpServletResponse response )
	{
		DynaTree dynaTree = new DynaTree( "root", null, true, "/", null );
		// get full path
		String fullPath = request.getSession().getServletContext().getRealPath( nonLiteralValueConstraintsPath );
		System.out.println( fullPath );

		dynaTree.setChildren( FileHelper.convertDirectoryToDynaTree( new File( fullPath ), "", true ) );
		return dynaTree.getChildren();
	}

	@RequestMapping( value = "/file_details", method = RequestMethod.POST )
	public @ResponseBody
	FileMeta getFileDetails( @RequestParam( "filePath" ) String filePath, HttpServletRequest request, HttpServletResponse response )
	{
		// get full path
		String fullPath = request.getSession().getServletContext().getRealPath( nonLiteralValueConstraintsPath );

		// return FileHelper.getFileDetails( request, absolutePath,
		// dspResourcePath + filePath );
		return FileHelper.getFileDetails( request, fullPath, filePath );
	}

	/**
	 * Upload document via jquery ajax file upload
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping( value = "/upload", method = RequestMethod.POST )
	public @ResponseBody
	LinkedList<FileMeta> multiUpload( MultipartHttpServletRequest request, HttpServletResponse response )
	{
		// get full path
		String fullPath = request.getSession().getServletContext().getRealPath( "/" );

		// build an iterator
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		// get each file
		while (itr.hasNext())
		{
			// get next MultipartFile
			mpf = request.getFile( itr.next() );
			// upload file and get the file back
			fileMeta = FileHelper.uploadFile( request, mpf, fullPath, dspFileUploadPath );

			// add to linkedList
			files.add( fileMeta );
		}
		return files;
	}

	@RequestMapping( value = "/getuploaded", method = RequestMethod.GET )
	public @ResponseBody
	LinkedList<FileMeta> getUploaded()
	{
		return files;
	}

	@RequestMapping( value = "/deleteFile", method = RequestMethod.POST )
	public @ResponseBody
	String deleteFile( @RequestParam( value = "fileRelativePath" ) String fileRelativePath )
	{
		// get file name
		String fileName = null;
		String[] splitRelativePath = fileRelativePath.split( "/" );
		fileName = splitRelativePath[splitRelativePath.length - 1];

		// removing files from the list
		Iterator<FileMeta> iteratorStudy = files.iterator();
		while (iteratorStudy.hasNext())
			if ( iteratorStudy.next().getFileName().equalsIgnoreCase( fileName ) )
				iteratorStudy.remove();

		// remove file from the local drive.
		FileHelper.deleteFile( dspFileUploadPath + fileRelativePath );

		return "success";
	}

	@RequestMapping( value = "/tab1", method = RequestMethod.POST )
	public ModelAndView namespaceDeclarations( /* tab2 get content via ajax */
	@RequestParam( value = "namespaceDeclarations", required = false ) String namespaceDeclarations, @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "owl2-tab2", "link", "owl2" );

		return model;
	}

	/* multiple upload files */
	@RequestMapping( value = "/tab2", method = RequestMethod.POST )
	public ModelAndView constraints( @RequestParam( "constraints" ) String constraints, @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "owl2-tab3", "link", "owl2" );

		return model;
	}

	@RequestMapping( value = "/tab3", method = RequestMethod.POST )
	public ModelAndView data( @RequestParam( "data" ) String data, @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "owl2-tab4", "link", "owl2" );

		return model;
	}

	@RequestMapping( value = "/tab4", method = RequestMethod.POST )
	public ModelAndView inferenceRules( @RequestParam( "inferenceRules" ) String inferenceRules, @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "owl2-tab5", "link", "owl2" );

		// escape < and >
		String nd = validationEnvironment.getNamespaceDeclarations().replace( "<", "&lt;" ).replace( ">", "&gt;" );
		String c = validationEnvironment.getConstraints().replace( "<", "&lt;" ).replace( ">", "&gt;" );
		String d = validationEnvironment.getData().replace( "<", "&lt;" ).replace( ">", "&gt;" );
		String ir = validationEnvironment.getInferenceRules().replace( "<", "&lt;" ).replace( ">", "&gt;" );

		model.addObject( "namespaceDeclarations", nd );
		model.addObject( "constraints", c );
		model.addObject( "data", d );
		model.addObject( "inferenceRules", ir );

		// add line separators at the end of each input graph
		String ND = new StringBuilder( validationEnvironment.getNamespaceDeclarations() ).append( "\r\n" ).toString();
		String C = new StringBuilder( validationEnvironment.getConstraints() ).append( "\r\n" ).toString();
		String D = new StringBuilder( validationEnvironment.getData() ).append( "\r\n" ).toString();
		String IR = new StringBuilder( validationEnvironment.getInferenceRules() ).append( "\r\n" ).toString();

		// input graph
		String rdfGraph = new StringBuilder( ND ).append( C ).append( D ).append( IR ).toString();

		Spin spin = new Spin( "OWL2_SPIN-Mapping.ttl" );
		spin.runInferences_checkConstraints( rdfGraph );

		model.addObject( "dspValidationResult", spin.validationResults );
		model.addObject( "constraintViolationList", spin.getConstraintViolationList() );

		return model;
	}
}
