package RDFValidation.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import RDFValidation.Spin;
import RDFValidation.ValidationEnvironment;

@Controller
@SessionAttributes( "validationEnvironment" )
@RequestMapping( value = "/dsp" )
public class DSPController
{
	// tab 0
	@RequestMapping( method = RequestMethod.GET )
	public ModelAndView landing( 
		@RequestParam( value = "sessionid", required = false ) 
		final String sessionId, final HttpServletResponse response )
	{
		ModelAndView model = new ModelAndView( "dsp", "link", "update" );

		if ( sessionId != null && sessionId.equals( "0" ) )
			response.setHeader( "SESSION_INVALID", "yes" );
		
		// initialize session variable
		model.addObject( "validationEnvironment", new ValidationEnvironment() );

		return model;
	}
	
	@RequestMapping( value = "/tab1", method = RequestMethod.POST )
	public ModelAndView namespaceDeclarations( /* tab2 get content via ajax*/
			@RequestParam( "namespaceDeclarations" ) String namespaceDeclarations, 
			@ModelAttribute( "validationEnvironment") ValidationEnvironment validationEnvironment ) 
	{
		ModelAndView model = new ModelAndView( "dsp-tab2", "link", "spss" );
		
		return model;
	}

	@RequestMapping( value = "/tab2", method = RequestMethod.POST )
	public ModelAndView constraints( 
			@RequestParam( "constraints" ) String constraints,
			@ModelAttribute( "validationEnvironment") ValidationEnvironment validationEnvironment ) 
	{
		ModelAndView model = new ModelAndView( "dsp-tab3", "link", "spss" );
		
		return model;
	}
	
	@RequestMapping( value = "/tab3", method = RequestMethod.POST )
	public ModelAndView data(
			@RequestParam( "data" ) String data,
			@ModelAttribute( "validationEnvironment") ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-tab4", "link", "spss" );

		return model;
	}
	
	@RequestMapping( value = "/tab4", method = RequestMethod.POST )
	public ModelAndView inferenceRules(
			@RequestParam( "inferenceRules" ) String inferenceRules,
			@ModelAttribute( "validationEnvironment") ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "dsp-tab5", "link", "spss" );
		
		// escape < and >
		String nd = validationEnvironment.getNamespaceDeclarations()
			.replace( "<", "&lt;" )
			.replace( ">", "&gt;" );
		String c = validationEnvironment.getConstraints()
			.replace( "<", "&lt;" )
			.replace( ">", "&gt;" );
		String d = validationEnvironment.getData()
			.replace( "<", "&lt;" )
			.replace( ">", "&gt;" );
		String ir = validationEnvironment.getInferenceRules()
			.replace( "<", "&lt;" )
			.replace( ">", "&gt;" );
		
		model.addObject( "namespaceDeclarations", nd );
		model.addObject( "constraints", c );
		model.addObject( "data", d );
		model.addObject( "inferenceRules", ir );
		
//		// namespace declarations ( delete comments )
//		int indexCommentStarts = 0;
//		int indexCommentEnds = 0;
//		
//		// save all indexCommentStart
//		
//		while ( nd.contains( "#" ) )
//		{
//			indexCommentStarts = nd.indexOf( "#" );
//			
//			// # before > for namespace declarations
//			if ( nd.contains( "&gt;" ) )
//			{
//				if ( indexCommentStarts == nd.indexOf( "&gt;", indexCommentStarts ) - 1 )
//				{
//					continue;
//				}
//			}
//			indexCommentEnds = nd.indexOf( "\r\n", nd.indexOf( "#" ) );
//			nd = nd.replace( nd.substring( indexCommentStarts, indexCommentEnds ), "" );
//		}
//		System.out.println(nd);
//		// constraints ( delete comments )
//		String c = validationEnvironment.getConstraints();
//		while ( c.contains( "#" ) )
//		{
//			c = c.replace( c.substring( c.indexOf( "#" ), c.indexOf( "\r\n", c.indexOf( "#" ) ) ), "" );
//		}
//		System.out.println(c);
//		// data ( delete comments )
//		String d = validationEnvironment.getData();
//		while ( d.contains( "#" ) )
//		{
//			d = d.replace( d.substring( d.indexOf( "#" ), d.indexOf( "\r\n", d.indexOf( "#" ) ) ), "" );
//		}
//		System.out.println(d);
//		// inference rules ( delete comments )
//		String ir = validationEnvironment.getInferenceRules();
//		while ( ir.contains( "#" ) )
//		{
//			ir = ir.replace( ir.substring( ir.indexOf( "#" ), ir.indexOf( "\r\n", ir.indexOf( "#" ) ) ), "" );
//		}
//		System.out.println(ir);
		String rdfGraph = new StringBuilder( 
			validationEnvironment.getNamespaceDeclarations() )
			.append( validationEnvironment.getConstraints() )
			.append( validationEnvironment.getData() )
			.append( validationEnvironment.getInferenceRules() )
			.toString();
		
		Spin spin = new Spin( "DSP_SPIN-Mapping.ttl" );
		spin.runInferences_checkConstraints( rdfGraph );
    	
		model.addObject( "dspValidationResult", spin.validationResults );
		model.addObject( "constraintViolationList", spin.getConstraintViolationList() );

		return model;
	}	
}
