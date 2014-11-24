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

import RDFValidation.JenaSPARQLQueries;
import RDFValidation.Spin;
import RDFValidation.SpinSKOS;
import RDFValidation.ValidationEnvironment;

@Controller
@SessionAttributes( "validationEnvironment" )
@RequestMapping( value = "/skos" )
public class SKOSController
{
	/* multiple file upload */
	LinkedList<FileMeta> files = new LinkedList<FileMeta>();

	/* file */
	FileMeta fileMeta = null;

	/* resource skos path */
	final String skosResourcePath = "/resources/rdfGraphs/SKOS/";
	final String fileUploadPath = "/resources/uploaded_files/";

	// skos main
	@RequestMapping( method = RequestMethod.GET )
	public ModelAndView landing( @RequestParam( value = "sessionid", required = false ) final String sessionId, final HttpServletResponse response )
	{
		ModelAndView model = new ModelAndView( "skos-main", "link", "skos" );

		if ( sessionId != null && sessionId.equals( "0" ) )
			response.setHeader( "SESSION_INVALID", "yes" );

		// initialize session variable
		model.addObject( "validationEnvironment", new ValidationEnvironment() );
		
		List<String> thesauri = new ArrayList<String>();
		thesauri.add( "TheSoz" );
		thesauri.add( "STW Thesaurus for Economics" );
		thesauri.add( "AGROVOC" );
		thesauri.add( "DBpedia" );
		model.addObject("thesauri", thesauri);

		return model;
	}

	/**
	 * skos demo
	 */
	@RequestMapping( value = "/demo", method = RequestMethod.GET )
	public ModelAndView demo( @RequestParam( value = "sessionid", required = false ) final String sessionId, final HttpServletResponse response )
	{
		ModelAndView model = new ModelAndView( "skos-demo", "link", "skos" );
		
		return model;
	}

	@RequestMapping( value = "/demo/validation", method = RequestMethod.POST )
	public ModelAndView demo_tab3( 
		HttpServletRequest request,
		@RequestParam( "nameSpaceDeclaration" ) String nameSpaceDeclaration, 
		@RequestParam( "constraints" ) String constraints, 
		@RequestParam( "data" ) String data,
		@RequestParam( value = " thesauri", required = false ) final String[] thesauri )
	{
		ModelAndView model = new ModelAndView( "skos-demo-validation", "link", "skos" );

		/*
		 * escape < and > String nd =
		 * validationEnvironment.getNamespaceDeclarations().replace( "<", "&lt;"
		 * ).replace( ">", "&gt;" ); String c =
		 * validationEnvironment.getConstraints().replace( "<", "&lt;"
		 * ).replace( ">", "&gt;" ); String d =
		 * validationEnvironment.getData().replace( "<", "&lt;" ).replace( ">",
		 * "&gt;" );
		 */

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy HH.mm.ss");
		System.out.println( "start: " +  sdf.format( new java.util.Date() ) + " ..." );
		
		// add line separators at the end of each input graph
		String ND = new StringBuilder( nameSpaceDeclaration ).append( "\r\n" ).toString();
		String C = new StringBuilder( constraints ).append( "\r\n" ).toString();
		String D = new StringBuilder( data ).append( "\r\n" ).toString();

		// input graph
		String rdfGraph = new StringBuilder( ND ).append( C ).append( D ).toString();
//
//		SpinSKOS spin = new SpinSKOS( "SKOS_SPIN-Mapping.ttl" );
//		
//		// thesauri ( zip files )
//		String absolutePathResources = request.getSession().getServletContext().getRealPath( "/resources" );
//		spin.setAbsolutePathResources( absolutePathResources );
////		if( thesauri != null && thesauri.length() > 0 )
////		{
////			
////		}
//		List<String> zipFiles = new ArrayList<String>();
//		zipFiles.add( "thesoz_0_93.zip" );
//		spin.setZipFiles( zipFiles );
//		
//		spin.runInferences_checkConstraints( rdfGraph );
//
//		model.addObject( "skosValidationResult", spin.validationResults );
//		model.addObject( "constraintViolationList", spin.getConstraintViolationList() );
//
//		// SPIN exception
//		if ( spin.getSPINException() != null )
//		{
//			model.addObject( "spinException", spin.getSPINException() );
//		}
		
		String absolutePathResources = request.getSession().getServletContext().getRealPath( "/resources" );
		boolean isLogging = true;
		JenaSPARQLQueries jenaSPARQLQueries = new JenaSPARQLQueries( absolutePathResources, rdfGraph, isLogging );
		jenaSPARQLQueries.query();
		
		model.addObject( "constraintViolationList", jenaSPARQLQueries.getConstraintViolationList() );
		
		System.out.println( "end: " + sdf.format( new java.util.Date() ) );

		return model;
	}

	/**
	 * skos UPLOAD
	 */
	@RequestMapping( value = "/upload", method = RequestMethod.GET )
	public ModelAndView uploadGraphInitial( /* tab1 get content via ajax */
	@RequestParam( value = "sessionid", required = false ) final String sessionId, final HttpServletResponse response )
	{
		ModelAndView model = new ModelAndView( "skos-upload", "link", "skos" );

//		String rdfGraph = 
//			"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> . @prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> . @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . @prefix owl: <http://www.w3.org/2002/07/owl#> .  @prefix dcterms: <http://purl.org/dc/terms#> . @prefix skos: <http://www.w3.org/2004/02/skos/core#> . @prefix foaf: <http://xmlns.com/foaf/0.1/#> . @prefix: <http://www.example.org/skos/data#> .";
//		
//		SpinSKOS spin = new SpinSKOS( "SKOS_SPIN-Mapping.ttl", "thesoz_0_93.ttl" );
//		spin.runInferences_checkConstraints( rdfGraph );
//
//		model.addObject( "skosValidationResult", spin.validationResults );
//		model.addObject( "constraintViolationList", spin.getConstraintViolationList() );
//
//		// SPIN exception
//		if ( spin.getSPINException() != null )
//		{
//			model.addObject( "spinException", spin.getSPINException() );
//		}
		
		return model;
	}

	/* skos upload validation */
	@RequestMapping( value = "/upload/validation", method = RequestMethod.POST )
	public ModelAndView uploadGraphValidation( HttpServletRequest request, HttpServletResponse response )
	{
		ModelAndView model = new ModelAndView( "skos-upload-validation", "link", "skos" );

		if ( this.files != null && this.files.size() > 0 )
		{
			String rdfGraph = "";
			for ( FileMeta fm : files )
			{
				// add file content
				rdfGraph += fm.getFileContent();
				rdfGraph += "\r\n";
			}

			// add predefined namespace declarations to RDF graph
			String absolutePath = request.getSession().getServletContext().getRealPath( skosResourcePath );
			absolutePath = absolutePath + "/" + "defaultNamespaceDeclarations.ttl";
			// get predefined namespace content and append to rdfGraph
			FileMeta fileMeta = FileHelper.getFileDetails( absolutePath );
			rdfGraph += fileMeta.getFileContent();

			Spin spin = new Spin( "SKOS_SPIN-Mapping.ttl" );
			spin.runInferences_checkConstraints( rdfGraph );

			model.addObject( "skosValidationResult", spin.validationResults );
			model.addObject( "constraintViolationList", spin.getConstraintViolationList() );

			// SPIN exception
			if ( spin.getSPINException() != null )
			{
				model.addObject( "spinException", spin.getSPINException() );
			}
		}
		return model;
	}

	/** END UPLOAD GRAPH */

	/**
	 * UTILITY METHODS
	 */

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
		// String absolutePath = this.getClass().getClassLoader().getResource(
		// "rdfGraphs" ).getPath();
		// absolutePath = absolutePath.substring( 1, absolutePath.length() - 1
		// );
		// String absolutePath =
		// request.getSession().getServletContext().getRealPath(
		// skosResourcePath
		// );
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

	@RequestMapping( value = "/deleteFile", method = RequestMethod.POST )
	public @ResponseBody
	String deleteFile( @RequestParam( value = "filename" ) String filename, HttpServletRequest request, HttpServletResponse response )
	{
		// removing files from the list
		Iterator<FileMeta> iteratorFiles = files.iterator();
		while (iteratorFiles.hasNext())
			if ( iteratorFiles.next().getFileName().equalsIgnoreCase( filename ) )
				iteratorFiles.remove();

		// String absolutePath =
		// request.getSession().getServletContext().getRealPath(
		// skosFileUploadPath );
		// remove file from the local drive.
		// FileHelper.deleteFile( absolutePath + filename );

		return "success";
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
	FileMeta getFileDetails( @RequestParam( "filePath" ) String filePath, @RequestParam( value = "additionalPath", required = false ) String additionalPath, HttpServletRequest request, HttpServletResponse response )
	{
		// String absolutePath = this.getClass().getClassLoader().getResource(
		// skosResourcePath ).getPath();
		// absolutePath = absolutePath.substring( 1, absolutePath.length() - 1
		// );
		// absolutePath = absolutePath + "/" + filePath;

		String absolutePath = request.getSession().getServletContext().getRealPath( additionalPath != null ? additionalPath : skosResourcePath );
		absolutePath = absolutePath + "/" + filePath;

		// return FileHelper.getFileDetails( request, absolutePath,
		// skosResourcePath + filePath );
		return FileHelper.getFileDetails( absolutePath );
	}

	/**
	 * Get the json structure of specific folder
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping( value = "/resource_structure", method = RequestMethod.POST )
	public @ResponseBody
	List<DynaTree> directoryStructure( @RequestParam( value = "specificDirectory", required = false ) String specificDirectory, HttpServletRequest request, HttpServletResponse response )
	{
		DynaTree dynaTree = new DynaTree( "root", null, true, "/", null );
		// get full path
		String fullPath = request.getSession().getServletContext().getRealPath( specificDirectory != null ? specificDirectory : skosResourcePath );
		// System.out.println( fullPath );

		// System.out.println(this.getClass().getClassLoader().getResource(
		// "rdfGraphs" ).getPath());

		// String absolutePath = this.getClass().getClassLoader().getResource(
		// "/rel.txt" ).getPath();
		// absolutePath = absolutePath.substring( 1, absolutePath.length() - 8
		// );
		// System.out.println( absolutePath );

		// System.out.println(absolutePath.lastIndexOf( "resources" ));

		// absolutePath = absolutePath.substring( 0, absolutePath.lastIndexOf(
		// "resources" ) - 9 ) + absolutePath.substring(
		// absolutePath.lastIndexOf( "resources" ), absolutePath.length() );

		// replace %20 in absolute path of web app
		// absolutePath = absolutePath.replace( "%20", " " );

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
		// "SPIN/functions/skos-functions.ttl" ).getPath(), null, true, "/",
		// null
		// ) );
		// dynaTree.setChildren( listDynaTree );

		// return json
		return dynaTree.getChildren();
	}

}
