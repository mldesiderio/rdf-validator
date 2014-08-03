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
@RequestMapping( value = "/owl2" )
public class OWL2Controller
{
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

	@RequestMapping( value = "/tab1", method = RequestMethod.POST )
	public ModelAndView namespaceDeclarations( /* tab2 get content via ajax */
	@RequestParam( "namespaceDeclarations" ) String namespaceDeclarations, @ModelAttribute( "validationEnvironment" ) ValidationEnvironment validationEnvironment )
	{
		ModelAndView model = new ModelAndView( "owl2-tab2", "link", "owl2" );

		return model;
	}

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
		String ND = new StringBuilder ( validationEnvironment.getNamespaceDeclarations() ).append( "\r\n" ).toString();
		String C = new StringBuilder ( validationEnvironment.getConstraints() ).append( "\r\n" ).toString();
		String D = new StringBuilder ( validationEnvironment.getData() ).append( "\r\n" ).toString();
		String IR = new StringBuilder ( validationEnvironment.getInferenceRules() ).append( "\r\n" ).toString();
		
		// input graph
		String rdfGraph = new StringBuilder( ND )
			.append( C )
			.append( D )
			.append( IR )
			.toString();
		
		Spin spin = new Spin( "OWL2_SPIN-Mapping.ttl" );
		spin.runInferences_checkConstraints( rdfGraph );
    	
		model.addObject( "dspValidationResult", spin.validationResults );
		model.addObject( "constraintViolationList", spin.getConstraintViolationList() );
				
		return model;
	}
}
