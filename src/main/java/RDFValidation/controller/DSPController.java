package RDFValidation.controller;

import helper.FileMeta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import RDFValidation.ValidationEnvironment;

@Controller
@SessionAttributes( "validationEnvironment" )
@RequestMapping( value = "/dsp" )
public class DSPController
{
	/* variables for files */
	LinkedList<FileMeta> files = new LinkedList<FileMeta>();
	FileMeta fileMeta = null;

	// tab 0
	@RequestMapping( method = RequestMethod.GET )
	public ModelAndView landing( @RequestParam( value = "sessionid", required = false ) final String sessionId, final HttpServletResponse response )
	{
		ModelAndView model = new ModelAndView( "dsp", "link", "dsp" );

		if ( sessionId != null && sessionId.equals( "0" ) )
			response.setHeader( "SESSION_INVALID", "yes" );

		// initialize session variable
		model.addObject( "validationEnvironment", new ValidationEnvironment() );

		return model;
	}

	@RequestMapping( value = "/upload", method = RequestMethod.POST )
	public @ResponseBody
	LinkedList<FileMeta> upload( MultipartHttpServletRequest request, HttpServletResponse response )
	{

		// 1. build an iterator
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		// 2. get each file
		while (itr.hasNext())
		{

			// 2.1 get next MultipartFile
			mpf = request.getFile( itr.next() );
			System.out.println( mpf.getOriginalFilename() + " uploaded! " + files.size() );

			// 2.2 if files > 10 remove the first from the list
			if ( files.size() >= 10 )
				files.pop();

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			fileMeta.setFileName( mpf.getOriginalFilename() );
			fileMeta.setFileSize( mpf.getSize() / 1024 + " Kb" );
			fileMeta.setFileType( mpf.getContentType() );

			try
			{
				fileMeta.setBytes( mpf.getBytes() );

				// copy file to local disk / relative to application context
				ServletContext sc = request.getSession().getServletContext();
				String fullPath = sc.getRealPath( "/resources/uploaded_files/" );

				FileCopyUtils.copy( mpf.getBytes(), new FileOutputStream( fullPath + "/" + mpf.getOriginalFilename() ) );

				String rdfGraphPath = sc.getRealPath( "/resources/rdfGraphs/" );
				FileUtils.writeStringToFile( new File( rdfGraphPath + "/" + "rdfGraph2.ttl" ), "TExt from textarea" );

			} catch (IOException e)
			{
				e.printStackTrace();
			}
			// if text plain
			if ( fileMeta.getFileType().equalsIgnoreCase( "text/plain" ) )
			{
				try
				{
					fileMeta.setFileContent( new String( fileMeta.getBytes(), "UTF-8" ) );
				} catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
			} else
				fileMeta.setFileContent( "unable to show binary file content" );
			// 2.4 add to files
			files.add( fileMeta );
		}
		// result will be like this
		// [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
		return files;
	}

	@RequestMapping( value = "/tab1", method = RequestMethod.POST )
	public ModelAndView namespaceDeclarations( /* tab2 get content via ajax */
	@RequestParam( "namespaceDeclarations" ) String namespaceDeclarations, @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-tab2", "link", "dsp" );

		return model;
	}

	@RequestMapping( value = "/tab2", method = RequestMethod.POST )
	public ModelAndView constraints( @RequestParam( "constraints" ) String constraints, @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-tab3", "link", "dsp" );

		return model;
	}

	@RequestMapping( value = "/tab3", method = RequestMethod.POST )
	public ModelAndView data( @RequestParam( "data" ) String data, @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-tab4", "link", "dsp" );

		return model;
	}

	@RequestMapping( value = "/tab4", method = RequestMethod.POST )
	public ModelAndView inferenceRules( @RequestParam( "inferenceRules" ) String inferenceRules, @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
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

		// // namespace declarations ( delete comments )
		// int indexCommentStarts = 0;
		// int indexCommentEnds = 0;
		//
		// // save all indexCommentStart
		//
		// while ( nd.contains( "#" ) )
		// {
		// indexCommentStarts = nd.indexOf( "#" );
		//
		// // # before > for namespace declarations
		// if ( nd.contains( "&gt;" ) )
		// {
		// if ( indexCommentStarts == nd.indexOf( "&gt;", indexCommentStarts ) -
		// 1 )
		// {
		// continue;
		// }
		// }
		// indexCommentEnds = nd.indexOf( "\r\n", nd.indexOf( "#" ) );
		// nd = nd.replace( nd.substring( indexCommentStarts, indexCommentEnds
		// ), "" );
		// }
		// System.out.println(nd);
		// // constraints ( delete comments )
		// String c = validationEnvironment.getConstraints();
		// while ( c.contains( "#" ) )
		// {
		// c = c.replace( c.substring( c.indexOf( "#" ), c.indexOf( "\r\n",
		// c.indexOf( "#" ) ) ), "" );
		// }
		// System.out.println(c);
		// // data ( delete comments )
		// String d = validationEnvironment.getData();
		// while ( d.contains( "#" ) )
		// {
		// d = d.replace( d.substring( d.indexOf( "#" ), d.indexOf( "\r\n",
		// d.indexOf( "#" ) ) ), "" );
		// }
		// System.out.println(d);
		// // inference rules ( delete comments )
		// String ir = validationEnvironment.getInferenceRules();
		// while ( ir.contains( "#" ) )
		// {
		// ir = ir.replace( ir.substring( ir.indexOf( "#" ), ir.indexOf( "\r\n",
		// ir.indexOf( "#" ) ) ), "" );
		// }
		// System.out.println(ir);
		String rdfGraph = new StringBuilder( validationEnvironment.getNamespaceDeclarations() ).append( validationEnvironment.getConstraints() ).append( validationEnvironment.getData() ).append( validationEnvironment.getInferenceRules() ).toString();

		// Spin spin = new Spin( "DSP_SPIN-Mapping.ttl" );
		// spin.runInferences_checkConstraints( rdfGraph );

		// model.addObject( "dspValidationResult", spin.validationResults );
		// model.addObject( "constraintViolationList",
		// spin.getConstraintViolationList() );

		// dummy loading process
		try
		{
			Thread.sleep( 10000 );
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return model;
	}
}
