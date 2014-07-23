package RDFValidation.ajax;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping( "/documentation/datapoints" )
@SessionAttributes( "datapoint" )
public class GetDatapoints
{
//	@RequestMapping( method = RequestMethod.GET )
//	public ModelAndView datapoints()
//	{
//		ModelAndView mav = new ModelAndView( "getDatapoints" );
//
//		List<String> studyGroups = persistenceStrategy.getStudyGroupDAO().getAllByPrefLabel();
//
//		mav.addObject( "studyGroups", studyGroups );
//
//		return mav;
//	}
//
//	@RequestMapping( value = "/{studyGroup}", method = RequestMethod.GET )
//	public ModelAndView periodOfTimes(
//			@PathVariable final String studyGroup,
//			@RequestParam final String level,
//			@ModelAttribute final Datapoint datapoint )
//	{
//		ModelAndView mav = new ModelAndView( "getDatapoints_study" );
//
//		mav.addObject( "level", level );
//
//		List<String> periodOfTimes = persistenceStrategy.getStudyGroupDAO().getPeriodOfTimesStartDateYear( studyGroup );
//
//		mav.addObject( "studies", periodOfTimes );
//
//		mav.addObject( "datapoint", datapoint.setStudyGroup( studyGroup ) );
//
//		return mav;
//	}
//
//	@RequestMapping( value = "/{studyGroup}/{study}", method = RequestMethod.GET )
//	public ModelAndView datasets(
//			@PathVariable final String studyGroup,
//			@PathVariable final String study,
//			@RequestParam final String level,
//			@ModelAttribute final Datapoint datapoint )
//	{
//		ModelAndView mav = new ModelAndView( "getDatapoints_datasets" );
//
//		mav.addObject( "level", level );
//
//		// exit here if level is series
//		if ( StringUtils.equals( "study", level ) )
//			return mav;
//
//		List<String> dataSetTypes = persistenceStrategy.getLogicalDataSetDAO().getDataSetTypes( studyGroup );
//		List<String> activeDataSetTypes = persistenceStrategy.getLogicalDataSetDAO().getDataSetTypes( studyGroup, study );
//
//		List<String> dataSetSubTypes = persistenceStrategy.getLogicalDataSetDAO().getDataSetSubTypes( studyGroup );
//
//		mav.addObject( "dataSetTypes", dataSetTypes );
//		mav.addObject( "activeDataSetTypes", activeDataSetTypes );
//
//		mav.addObject( "dataSetSubTypes", dataSetSubTypes );
//
//		mav.addObject( "datapoint", datapoint.setStudyGroup( studyGroup ).setStudy( study ) );
//
//		return mav;
//	}
//
//	@RequestMapping( value = "/{studyGroup}/{study}/{dataSetType}", method = RequestMethod.GET )
//	public ModelAndView datasets(
//			@PathVariable final String studyGroup,
//			@PathVariable final String study,
//			@PathVariable final String dataSetType,
//			@RequestParam final String level,
//			@ModelAttribute final Datapoint datapoint )
//	{
//		ModelAndView mav = new ModelAndView( "getDatapoints_datafiles" );
//
//		mav.addObject( "level", level );
//
//		// exit here if level is series
//		if ( StringUtils.equals( "study", level ) )
//			return mav;
//
//		List<String> dataSetTypes = persistenceStrategy.getLogicalDataSetDAO().getDataSetTypes( studyGroup );
//		List<String> activeDataSetTypes = persistenceStrategy.getLogicalDataSetDAO().getDataSetTypes( studyGroup, study );
//
//		List<String> dataSetSubTypes = persistenceStrategy.getLogicalDataSetDAO().getDataSetSubTypes( studyGroup );
//		List<String> activeDataSetSubTypes = persistenceStrategy.getLogicalDataSetDAO().getDataSetSubTypes( studyGroup, study, dataSetType );
//
//		mav.addObject( "dataSetTypes", dataSetTypes );
//		mav.addObject( "activeDataSetTypes", activeDataSetTypes );
//
//		mav.addObject( "dataSetSubTypes", dataSetSubTypes );
//		mav.addObject( "activeDataSetSubTypes", activeDataSetSubTypes );
//
//		mav.addObject( "datapoint", datapoint.setStudyGroup( studyGroup ).setStudy( study ).setDataSet( dataSetType ) );
//
//		return mav;
//	}
//	
//	@RequestMapping( value = "/{studyGroup}/{study}/{dataSetType}/{dataFile}", method = RequestMethod.GET )
//	public ModelAndView datasets(
//			@PathVariable final String studyGroup,
//			@PathVariable final String study,
//			@PathVariable final String dataSetType,
//			@PathVariable final String dataFile,
//			@RequestParam final String level,
//			@ModelAttribute final Datapoint datapoint )
//	{
//		ModelAndView mav = new ModelAndView( "getDatapoints_groupVariables" );
//
//		mav.addObject( "level", level );
//		
//		List<LogicalDataSet> activeLogicalDataSets = new ArrayList<LogicalDataSet>();
//		
//		if ( StringUtils.equals( "undefined", dataFile ) )
//			activeLogicalDataSets = persistenceStrategy.getLogicalDataSetDAO().getActiveLogicalDataSet( studyGroup, study, dataSetType );
//		else
//			activeLogicalDataSets = persistenceStrategy.getLogicalDataSetDAO().getActiveLogicalDataSet( studyGroup, study, dataSetType, dataFile );
//
//		if ( activeLogicalDataSets.size() == 0)
//			return mav;
//		
//		LogicalDataSet logicalDataSet = activeLogicalDataSets.get(0);
//		List<Variable> variables = logicalDataSet.getContainsVariable();
//
//		mav.addObject( "groupVariables", variables );
//		mav.addObject( "datapoint", datapoint.setStudyGroup( studyGroup ).setStudy( study ).setDataSet( dataSetType ) );
//
//		return mav;
//	}

}
