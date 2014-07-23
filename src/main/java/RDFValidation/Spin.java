package RDFValidation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.topbraid.spin.constraints.ConstraintViolation;
import org.topbraid.spin.constraints.SPINConstraints;
import org.topbraid.spin.constraints.SimplePropertyPath;
import org.topbraid.spin.inference.SPINInferences;
import org.topbraid.spin.model.TemplateCall;
import org.topbraid.spin.system.SPINLabels;
import org.topbraid.spin.system.SPINModuleRegistry;
import org.topbraid.spin.util.JenaUtil;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * 
 * @author Thomas Bosch
 *
 */
public class Spin 
{
	// relative path ( web application )
	private String relPathWebApp = null;
	
	// SPIN mappings
	private Set<String> spinMappings = null;
	
	// validation results
	public StringBuilder validationResults = null;
	
	// constraint violation list
	private List<RDFValidation.ConstraintViolation> constraintViolationList = null;
	private RDFValidation.ConstraintViolation constraintViolation = null;
	private List<String> constraintViolationPaths = null;
	private List<String> constraintViolationFixes = null;
	
	// inferred triples
	public PrintWriter writerInferredTriples = null;
	
	// locally stored SPIN-related templates and functions
	private OntModel ontModel_TemplatesFunctions = null;
	
	/**
	 * 
	 * 
	 */
	public Spin( String... spinMappings )
	{
		// initialization
		init( spinMappings );
	}
	
	/**
	 * initialization
	 * 
	 */
	private void init( String... spinMappings )
	{
		// SPIN mappings
		 this.spinMappings = new HashSet<String>();
		 for ( String spinMapping : spinMappings ) 
		 {
		     this.spinMappings.add( spinMapping );
		 }
		
		// validation results
		validationResults = new StringBuilder();
		
		// constraint violation list
		constraintViolationList = new ArrayList<RDFValidation.ConstraintViolation>();
        
		// initialize SPIN system functions ( such as sp:gt (>) ) and templates
		SPINModuleRegistry.get().init();
		
		// relative path ( web application )
		ClassLoader loader = Spin.class.getClassLoader();
		relPathWebApp = loader.getResource("RDFValidation/Spin.class").toString().substring( loader.getResource("RDFValidation/Spin.class").toString().indexOf( "file:\\" ) + 7, loader.getResource("RDFValidation/Spin.class").toString().lastIndexOf( "WEB-INF" ) );
		
		// locally stored SPIN-related templates and functions
		Model graph_TemplatesFunctions = getRDFGraph( relPathWebApp + "WEB-INF\\SPIN\\functions\\functions.ttl", "TTL" ); 
		graph_TemplatesFunctions.add( getRDFGraph( relPathWebApp + "WEB-INF\\SPIN\\functions\\dsp-functions.ttl", "TTL" ) );
		ontModel_TemplatesFunctions = JenaUtil.createOntologyModel( OntModelSpec.OWL_MEM, graph_TemplatesFunctions );			
	}
	
	/**
	 * run inferences and check constraints
	 * 
	 * 
	 */
	public void runInferences_checkConstraints( String... rdfGraphs )
	{
//		// fill RDF graph ( namespace declarations, constraints, data, inference rules ) 
//		Model graph = getRDFGraphByString( rdfGraphs[0], "TTL" ); // concrete syntaxes: TTL, RDF/XML, ...
//		for ( int i = 1; i < rdfGraphs.length; i++)
//		{
//			graph.add( getRDFGraphByString( rdfGraphs[i], "TTL" ) );
//		}
		
		// fill RDF graph ( namespace declarations, constraints, data, inference rules )
		Model graph = getRDFGraph( relPathWebApp + "resources\\rdfGraphs\\rdfGraph.ttl", "TTL" );
		
		// fill RDF graph ( SPIN mappings )
		for ( String spinMapping : spinMappings ) 
		{
			graph.add( getRDFGraph( relPathWebApp + "WEB-INF\\SPIN\\SPIN-mappings\\" + spinMapping, "TTL" ) );
		}
		
		// create OntModel with imports
		OntModel ontModel = JenaUtil.createOntologyModel( OntModelSpec.OWL_MEM, graph );
		
		// create and add model for inferred triples
		Model newTriples = ModelFactory.createDefaultModel();
		ontModel.addSubModel( newTriples );
		
		// add locally stored SPIN-related templates and functions to model
		ontModel.add( ontModel_TemplatesFunctions );
		
		// register locally stored SPIN-related templates and functions
		SPINModuleRegistry.get().registerAll( ontModel, null );
		
		// run all inferences
		SPINInferences.run( ontModel, newTriples, null, null, false, null );
        try 
		{
			writerInferredTriples = new PrintWriter( "UTF-8" );
		} 
		catch ( FileNotFoundException e ) { e.printStackTrace(); }
		newTriples.write( writerInferredTriples, "TTL" );
		
		// check constraints
		List<ConstraintViolation> constraintViolations = SPINConstraints.check( ontModel, null );
		
		// constraint violations
		validationResults.append( "Constraint violations" );
		validationResults.append( "<br/>" );
		validationResults.append( "---------------------" );
		validationResults.append( System.getProperty("line.separator") );
		validationResults.append( System.getProperty("line.separator") );
		for( ConstraintViolation cv : constraintViolations ) 
		{
			constraintViolation = new RDFValidation.ConstraintViolation();
			constraintViolation.setRoot( SPINLabels.get().getLabel( cv.getRoot() ) );
			constraintViolation.setMessage( cv.getMessage() );
			constraintViolation.setSource( SPINLabels.get().getLabel( cv.getSource() ) );
			
			validationResults.append( " - source: " ).append( SPINLabels.get().getLabel( cv.getSource() ) );
			validationResults.append( System.getProperty("line.separator") );
			validationResults.append( " - root: " ).append( SPINLabels.get().getLabel( cv.getRoot() ) );
			validationResults.append( System.getProperty("line.separator") );
			validationResults.append( " - message: " ).append( cv.getMessage() );
			validationResults.append( System.getProperty("line.separator") );
			
			constraintViolationPaths = new ArrayList<String>();
//			for ( int i = 0; i <= 5; i++ )
//			{
//				constraintViolationPaths.add( String.valueOf( i ) );
//			}
			if( cv.getPaths().size() == 0 )
			{
				constraintViolationPaths.add( "" );
			}
			for ( SimplePropertyPath violationPath : cv.getPaths() ) 
			{
				constraintViolationPaths.add( violationPath.toString() );
				
				validationResults.append( " - path: " ).append( violationPath.toString() );
				validationResults.append( System.getProperty("line.separator") );
		    }
			constraintViolation.setPaths( constraintViolationPaths );
			
			constraintViolationFixes = new ArrayList<String>();
//			for ( int i = 0; i <= 5; i++ )
//			{
//				constraintViolationFixes.add( String.valueOf( i ) );
//			}
			if( cv.getFixes().size() == 0 )
			{
				constraintViolationFixes.add( "" );
			}
			for ( TemplateCall violationFix : cv.getFixes() ) 
			{
				constraintViolationFixes.add( violationFix.toString() );
		    }
			constraintViolation.setFixes( constraintViolationFixes );
			
			validationResults.append( " - # fixes: " ).append( cv.getFixes().size() );
	        validationResults.append( System.getProperty("line.separator") );
	        validationResults.append( System.getProperty("line.separator") );
	        
	        constraintViolationList.add( constraintViolation );
		}
		
		validationResults.append( "violation root | violation message | # violation path | # violation fixes" );
		validationResults.append( System.getProperty("line.separator") );
		validationResults.append( "-------------------------------------------------------------------------" );
		validationResults.append( System.getProperty("line.separator") );
		validationResults.append( System.getProperty("line.separator") );
		for( ConstraintViolation cv : constraintViolations ) 
		{
			validationResults.append( SPINLabels.get().getLabel( cv.getRoot() ) ).append( " | " );
			validationResults.append( cv.getMessage() ).append( " | " );
			for ( SimplePropertyPath violationPath : cv.getPaths() ) 
			{
				validationResults.append( violationPath.toString() ).append( " | " );
		    }
			validationResults.append( cv.getFixes().size() );
	        validationResults.append( System.getProperty("line.separator") );
		}
	}

	/**
	 * get RDF graph
	 * 
	 * @param rdfGraph_AbsolutePathAndFileName
	 * @param rdfGraph_ConcreteSyntax
	 */
	public Model getRDFGraph( String rdfGraph_AbsolutePathAndFileName, String rdfGraph_ConcreteSyntax )
	{
		Model model = ModelFactory.createDefaultModel();
		
		try 
		{
			model.read( new FileInputStream( rdfGraph_AbsolutePathAndFileName ), null, rdfGraph_ConcreteSyntax );
		} 
		catch (FileNotFoundException e) { e.printStackTrace(); }
		
		return model;
	}
	
	/**
	 * get RDF graph ( by string )
	 * 
	 */
	public Model getRDFGraphByString( String rdfGraph, String rdfGraph_ConcreteSyntax )
	{
		Model model = ModelFactory.createDefaultModel();
		
		try 
		{
//			InputStream stream = new ByteArrayInputStream( rdfGraph.getBytes("UTF-8"));
//			model.read( stream, null, rdfGraph_ConcreteSyntax );
			model.read( IOUtils.toInputStream( rdfGraph, "UTF-8" ), null, rdfGraph_ConcreteSyntax );
		} 
//		catch (FileNotFoundException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		
		return model;
	}
	
	public List<RDFValidation.ConstraintViolation> getConstraintViolationList() 
	{
		return constraintViolationList;
	}
}
