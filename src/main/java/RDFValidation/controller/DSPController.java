package RDFValidation.controller;

import helper.DynaTree;
import helper.FileHelper;
import helper.FileMeta;

import java.io.File;
import java.util.ArrayList;
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

import RDFValidation.FileInputGraph;
import RDFValidation.Spin;
import RDFValidation.ValidationEnvironment;

@Controller
@SessionAttributes( "validationEnvironment" )
@RequestMapping( value = "/dsp" )
public class DSPController
{
	/* multiple file upload */
	LinkedList<FileMeta> files = new LinkedList<FileMeta>();
	
	/* file */
	FileMeta fileMeta = null;

	/* resource dsp path */
	final String dspResourcePath = "/resources/rdfGraphs/DSP";
	final String fileUploadPath = "/resources/uploaded_files/";

	// DSP main
	@RequestMapping( method = RequestMethod.GET )
	public ModelAndView landing( @RequestParam( value = "sessionid", required = false ) final String sessionId, final HttpServletResponse response )
	{
		ModelAndView model = new ModelAndView( "dsp-main", "link", "dsp" );

		if ( sessionId != null && sessionId.equals( "0" ) )
			response.setHeader( "SESSION_INVALID", "yes" );

		// initialize session variable
		model.addObject( "validationEnvironment", new ValidationEnvironment() );

		return model;
	}

	/**
	 * DSP 1 GRAPH (AJAX)
	 */
	/* DSP N graph Initial content */
	@RequestMapping( value = "/onegraph", method = RequestMethod.GET )
	public ModelAndView oneGraphInitial( /* tab1 get content via ajax */
	@RequestParam( value = "sessionid", required = false ) final String sessionId, final HttpServletResponse response )
	{
		ModelAndView model = new ModelAndView( "dsp-one", "link", "dsp" );

		if ( sessionId != null && sessionId.equals( "0" ) )
			response.setHeader( "SESSION_INVALID", "yes" );

		return model;
	}

	/* DSP N graph First Tab Submit */
	@RequestMapping( value = "/onegraph/tab1", method = RequestMethod.POST )
	public ModelAndView pneGraphValidation( /* tab2 get content via ajax */
//	@RequestParam( "rdfGraph" ) String rdfGraph
	)
	{
		ModelAndView model = new ModelAndView( "dsp-one-tab2", "link", "dsp" );
		
		// populate input graph
		String rdfGraph = "";
		for ( FileMeta file : files )
		{
			// add file content
			rdfGraph += file.getFileContent();
			rdfGraph += "\r\n";
		}
		
		Spin spin = new Spin( "DSP_SPIN-Mapping.ttl" );
		spin.runInferences_checkConstraints( rdfGraph );

		model.addObject( "dspValidationResult", spin.validationResults );
		model.addObject( "constraintViolationList", spin.getConstraintViolationList() );

		// input graph
		List<FileInputGraph> fileInputGraphList = new ArrayList<FileInputGraph>(files.size());
		FileInputGraph fileInputGraph = null;
		for ( FileMeta file : files )
		{
			fileInputGraph = new FileInputGraph();
			fileInputGraph.setFilename( file.getFileName() );
			fileInputGraph.setInputGraph( file.getFileContent().replace( "<", "&lt;" ).replace( ">", "&gt;" ) ); // escape < and >
			fileInputGraphList.add( fileInputGraph );
		}
		model.addObject( "fileInputGraphList", fileInputGraphList );

		return model;
	}

	/** END 1 GRAPH */

	/**
	 * DSP N GRAPH (AJAX)
	 */
	/* DSP N graph Initial content */
	@RequestMapping( value = "/ngraph", method = RequestMethod.GET )
	public ModelAndView nGraphInitial( /* tab2 get content via ajax */
		@RequestParam( value = "sessionid", required = false ) final String sessionId, 
		final HttpServletResponse response )
	{
		ModelAndView model = new ModelAndView( "dsp", "link", "dsp" );

		if ( sessionId != null && sessionId.equals( "0" ) )
			response.setHeader( "SESSION_INVALID", "yes" );

		return model;
	}

	/* DSP N graph First Tab Submit */
	@RequestMapping( value = "/ngraph/tab1", method = RequestMethod.POST )
	public ModelAndView namespaceDeclarations( /* tab2 get content via ajax */
		@RequestParam( "namespaceDeclarations" ) String namespaceDeclarations, 
		@ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-tab2", "link", "dsp" );

		return model;
	}

	/* DSP N graph Second Tab Submit */
	@RequestMapping( value = "/ngraph/tab2", method = RequestMethod.POST )
	public ModelAndView constraints( 
		@RequestParam( "constraints" ) String constraints, 
		@ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-tab3", "link", "dsp" );

		return model;
	}

	/* DSP N graph Third Tab Submit */
	@RequestMapping( value = "/ngraph/tab3", method = RequestMethod.POST )
	public ModelAndView data( 
		@RequestParam( "data" ) String data, 
		@ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-tab4", "link", "dsp" );

		return model;
	}

	/* DSP N graph Fourth Tab Submit */
	@RequestMapping( value = "/ngraph/tab4", method = RequestMethod.POST )
	public ModelAndView inferenceRules( 
		@RequestParam( "inferenceRules" ) String inferenceRules, 
		@ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-tab5", "link", "spss" );

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

		Spin spin = new Spin( "DSP_SPIN-Mapping.ttl" );
		spin.runInferences_checkConstraints( rdfGraph );

		model.addObject( "dspValidationResult", spin.validationResults );
		model.addObject( "constraintViolationList", spin.getConstraintViolationList() );

		// debugging
		// model.addObject( "debugging", spin.debugging );

		return model;
	}

	/** END N GRAPH */

	/**
	 * DSP EXAMPLE GRAPH (AJAX)
	 */

	/* DSP N graph Initial content */
	@RequestMapping( value = "/exmpgraph", method = RequestMethod.GET )
	public ModelAndView exmpGraphInitial( /* tab2 get content via ajax */
	@RequestParam( value = "sessionid", required = false ) final String sessionId, final HttpServletResponse response )
	{
		ModelAndView model = new ModelAndView( "dsp-exmp", "link", "dsp" );

		if ( sessionId != null && sessionId.equals( "0" ) )
			response.setHeader( "SESSION_INVALID", "yes" );

		return model;
	}

	/* DSP N graph First Tab Submit */
	@RequestMapping( value = "/exmpgraph/tab1", method = RequestMethod.POST )
	public ModelAndView exmpSecondTab( /* tab2 get content via ajax */
	@RequestParam( "filePath" ) String filePath, HttpServletRequest request )
	{
		ModelAndView model = new ModelAndView( "dsp-exmp-tab2", "link", "dsp" );

//		String absolutePath = this.getClass().getClassLoader().getResource( "rdfGraphs" ).getPath();
//		absolutePath = absolutePath.substring( 1, absolutePath.length() - 1 );
		
		String absolutePath = new StringBuilder( request.getSession().getServletContext().getRealPath( dspResourcePath ) ).append( "/" ).append( filePath ).toString();

		// System.out.println("absolutePath: " + absolutePath);
		// System.out.println("dspResourcePath + filePath: " + dspResourcePath +
		// filePath);

		// FileMeta fileMeta = FileHelper.getFileDetails( request, absolutePath,
		// dspResourcePath + filePath );
		FileMeta fileMeta = FileHelper.getFileDetails( absolutePath );

		model.addObject( "fileContent", fileMeta.getFileContent() );

		return model;
	}

	/* DSP N graph Second Tab Submit */
	@RequestMapping( value = "/exmpgraph/tab2", method = RequestMethod.POST )
	public ModelAndView exmpThirdTab( @RequestParam( "rdfGraph" ) String rdfGraph )
	{
		ModelAndView model = new ModelAndView( "dsp-exmp-tab3", "link", "dsp" );

		// escape < and >
		String rdfGraphInput = rdfGraph.replace( "<", "&lt;" ).replace( ">", "&gt;" );

		model.addObject( "rdfGraph", rdfGraphInput );

		Spin spin = new Spin( "DSP_SPIN-Mapping.ttl" );
		spin.runInferences_checkConstraints( rdfGraph );

		model.addObject( "dspValidationResult", spin.validationResults );
		model.addObject( "constraintViolationList", spin.getConstraintViolationList() );

		return model;
	}

	/** END EXAMPLE GRAPH */

	/**
	 * Upload document via jquery ajax file upload
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping( value = "/upload", method = RequestMethod.POST )
	public @ResponseBody
	FileMeta upload( MultipartHttpServletRequest request, HttpServletResponse response )
	{
		// absolute path
//		String absolutePath = this.getClass().getClassLoader().getResource( "rdfGraphs" ).getPath();
//		absolutePath = absolutePath.substring( 1, absolutePath.length() - 1 );
//		String absolutePath = request.getSession().getServletContext().getRealPath( dspResourcePath );
		String absolutePath = request.getSession().getServletContext().getRealPath( fileUploadPath );

		// build an iterator
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		// get each file
		while (itr.hasNext())
		{
			// get next MultipartFile
			mpf = request.getFile( itr.next() );
			// upload file and get the file back
			fileMeta = FileHelper.uploadFile( request, mpf, absolutePath, fileUploadPath );
		}
		return fileMeta;
	}
	
	/**
	 * Upload document via jquery ajax file upload
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping( value = "/multiple-file-upload", method = RequestMethod.POST )
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
			fileMeta = FileHelper.uploadFile( request, mpf, fullPath, fileUploadPath );

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

	/**
	 * get detail of document from specific folder
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping( value = "/file_details", method = RequestMethod.POST )
	public @ResponseBody
	FileMeta getFIleDetails( @RequestParam( "filePath" ) String filePath, HttpServletRequest request, HttpServletResponse response )
	{
//		String absolutePath = this.getClass().getClassLoader().getResource( dspResourcePath ).getPath();
//		absolutePath = absolutePath.substring( 1, absolutePath.length() - 1 );
//		absolutePath = absolutePath + "/" + filePath; 
		
		String absolutePath = request.getSession().getServletContext().getRealPath( dspResourcePath );
		absolutePath = absolutePath + "/" + filePath; 

		// return FileHelper.getFileDetails( request, absolutePath,
		// dspResourcePath + filePath );
		return FileHelper.getFileDetails( absolutePath );
	}

	/**
	 * Get the json structure of specific folder
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping( value = "/resource_structure", method = RequestMethod.GET )
	public @ResponseBody
	List<DynaTree> directoryStructure( HttpServletRequest request, HttpServletResponse response )
	{
		DynaTree dynaTree = new DynaTree( "root", null, true, "/", null );
		// get full path
		String fullPath = request.getSession().getServletContext().getRealPath( dspResourcePath );
//		System.out.println( fullPath );

		// System.out.println(this.getClass().getClassLoader().getResource(
		// "rdfGraphs" ).getPath());

//		String absolutePath = this.getClass().getClassLoader().getResource( "/rel.txt" ).getPath();
//		absolutePath = absolutePath.substring( 1, absolutePath.length() - 8 );
//		System.out.println( absolutePath );

		// System.out.println(absolutePath.lastIndexOf( "resources" ));

		// absolutePath = absolutePath.substring( 0, absolutePath.lastIndexOf(
		// "resources" ) - 9 ) + absolutePath.substring(
		// absolutePath.lastIndexOf( "resources" ), absolutePath.length() );

		// replace %20 in absolute path of web app
//		absolutePath = absolutePath.replace( "%20", " " );

		// dynaTree.addChild( new DynaTree( absolutePath, null, true, "/", null
		// ) );

		// String test = fullPath + "/";
		// get the directory structure
		// dynaTree.setChildren( convertDirectoryToDynaTree( new File(
		// "C:/Program Files/ApacheTomcat8/webapps/rdf-validation/WEB-INF/classes/rdfGraphs"
		// + "/" ), "" ) );
		// dynaTree.setChildren( convertDirectoryToDynaTree( new File(
		// "lelystad.informatik.uni-mannheim.de" + "/" ), "" ) );
		dynaTree.setChildren( FileHelper.convertDirectoryToDynaTree( new File( fullPath ), "", true ) );
		// dynaTree.setChildren( convertDirectoryToDynaTree( new File(
		// "127.0.0.1" + "/" ), "" ) );

		// testing
		// List<DynaTree> listDynaTree = new ArrayList<DynaTree>();
		// listDynaTree.add( new DynaTree(
		// this.getClass().getClassLoader().getResource( "/" +
		// "SPIN/functions/dsp-functions.ttl" ).getPath(), null, true, "/", null
		// ) );
		// dynaTree.setChildren( listDynaTree );

		// return json
		return dynaTree.getChildren();
	}

	/**
	 * DSP demo
	 */
	@RequestMapping( value = "/demo", method = RequestMethod.GET )
	public ModelAndView demo(
		@RequestParam( value = "sessionid", required = false ) 
		final String sessionId, 
		final HttpServletResponse response )
	{
		ModelAndView model = new ModelAndView( "dsp-demo", "link", "dsp" );

		if ( sessionId != null && sessionId.equals( "0" ) )
			response.setHeader( "SESSION_INVALID", "yes" );

		return model;
	}
	
	@RequestMapping( value = "/demo/tab1", method = RequestMethod.POST )
	public ModelAndView demo_tab1( 
		@RequestParam( "namespaceDeclarations" ) String namespaceDeclarations, 
		@ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-demo-tab2", "link", "dsp" );

		return model;
	}
	
	@RequestMapping( value = "/demo/tab2", method = RequestMethod.POST )
	public ModelAndView demo_tab2( 
		@RequestParam( "constraints" ) String constraints, 
		@ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-demo-tab3", "link", "dsp" );

		return model;
	}
	
	@RequestMapping( value = "/demo/tab3", method = RequestMethod.POST )
	public ModelAndView demo_tab3( 
		@RequestParam( "data" ) String data, 
		@ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-demo-tab4", "link", "dsp" );
		
		// escape < and >
		String nd = validationEnvironment.getNamespaceDeclarations().replace( "<", "&lt;" ).replace( ">", "&gt;" );
		String c = validationEnvironment.getConstraints().replace( "<", "&lt;" ).replace( ">", "&gt;" );
		String d = validationEnvironment.getData().replace( "<", "&lt;" ).replace( ">", "&gt;" );

		model.addObject( "namespaceDeclarations", nd );
		model.addObject( "constraints", c );
		model.addObject( "data", d );

		// add line separators at the end of each input graph
		String ND = new StringBuilder( validationEnvironment.getNamespaceDeclarations() ).append( "\r\n" ).toString();
		String C = new StringBuilder( validationEnvironment.getConstraints() ).append( "\r\n" ).toString();
		String D = new StringBuilder( validationEnvironment.getData() ).append( "\r\n" ).toString();

		// input graph
		String rdfGraph = new StringBuilder( ND ).append( C ).append( D ).toString();

		Spin spin = new Spin( "DSP_SPIN-Mapping.ttl" );
		spin.runInferences_checkConstraints( rdfGraph );

		model.addObject( "dspValidationResult", spin.validationResults );
		model.addObject( "constraintViolationList", spin.getConstraintViolationList() );

		return model;
	}
	
}
