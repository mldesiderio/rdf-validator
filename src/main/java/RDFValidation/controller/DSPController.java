package RDFValidation.controller;

import helper.FileHelper;
import helper.FileMeta;

import java.util.Iterator;

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

import RDFValidation.ValidationEnvironment;

@Controller
@SessionAttributes( "validationEnvironment" )
@RequestMapping( value = "/dsp" )
public class DSPController
{
	/* variables for files */
	FileMeta fileMeta = null;

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
	@RequestParam( "namespaceDeclarations" ) String namespaceDeclarations, @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-one-tab2", "link", "dsp" );

		return model;
	}

	/** END 1 GRAPH */

	/**
	 * DSP N GRAPH (AJAX)
	 */
	/* DSP N graph Initial content */
	@RequestMapping( value = "/ngraph", method = RequestMethod.GET )
	public ModelAndView nGraphInitial( /* tab2 get content via ajax */
	@RequestParam( value = "sessionid", required = false ) final String sessionId, final HttpServletResponse response )
	{
		ModelAndView model = new ModelAndView( "dsp", "link", "dsp" );

		if ( sessionId != null && sessionId.equals( "0" ) )
			response.setHeader( "SESSION_INVALID", "yes" );

		return model;
	}

	/* DSP N graph First Tab Submit */
	@RequestMapping( value = "/ngraph/tab1", method = RequestMethod.POST )
	public ModelAndView namespaceDeclarations( /* tab2 get content via ajax */
	@RequestParam( "namespaceDeclarations" ) String namespaceDeclarations, @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-tab2", "link", "dsp" );

		return model;
	}

	/* DSP N graph Second Tab Submit */
	@RequestMapping( value = "/ngraph/tab2", method = RequestMethod.POST )
	public ModelAndView constraints( @RequestParam( "constraints" ) String constraints, @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-tab3", "link", "dsp" );

		return model;
	}

	/* DSP N graph Third Tab Submit */
	@RequestMapping( value = "/ngraph/tab3", method = RequestMethod.POST )
	public ModelAndView data( @RequestParam( "data" ) String data, @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-tab4", "link", "dsp" );

		return model;
	}

	/* DSP N graph Fourth Tab Submit */
	@RequestMapping( value = "/ngraph/tab4", method = RequestMethod.POST )
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
	@ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-exmp-tab2", "link", "dsp" );

		return model;
	}

	/* DSP N graph Second Tab Submit */
	@RequestMapping( value = "/exmpgraph/tab2", method = RequestMethod.POST )
	public ModelAndView exmpThirdTab( @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-exmp-tab3", "link", "dsp" );

		return model;
	}

	/** END EXAMPLE GRAPH */

	@RequestMapping( value = "/upload", method = RequestMethod.POST )
	public @ResponseBody
	FileMeta upload( MultipartHttpServletRequest request, HttpServletResponse response )
	{
		// build an iterator
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;

		// get each file
		while (itr.hasNext())
		{
			// get next MultipartFile
			mpf = request.getFile( itr.next() );
			// upload file and get the file back
			fileMeta = FileHelper.uploadFile( request, mpf, "/resources/uploaded_files/" );
		}
		return fileMeta;
	}
}
