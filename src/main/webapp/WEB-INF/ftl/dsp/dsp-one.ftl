<h1>Description Set Profiles (DSP) - 1 Graph</h1>
<!-- the tabs -->
<div id="updateSpssTabContainer" class="tabContainer">
<ul style="height: 40px;">	
	<li><a class="tabElement" href="#dsp-tab1" style="text-decoration:none;padding: 10px 15px;"><span>1.</span> Namespace Declarations</a></li>
	<li><a class="tabElement" href="#dsp-tab2" style="text-decoration:none;padding: 10px 15px;"><span>2.</span> Validation Results</a></li>
</ul>

<div id="dsp-tab1" style="padding:0 !important; border:none; float:left; width:100%">
	<#include "dsp-one-tab1.ftl" />
</div>
<div id="dsp-tab2" style="padding:0 !important;border:none;float:left; width:100%">
	<#-- ajax content here -->
	<#include "dsp-one-tab2.ftl" />
</div>
					
<script>
	$jQ( function() // begin document ready
	{
		<#-- change the size of main content -->
		$jQ( "#col3" ).css( "margin", "0 0 0 15px");
		// global variable to indicate validation requirements
		validateDatapoint = true;
	
		<#-- jQery tab for main content 'define study, define spss file, import -->
		$jQ( "#updateSpssTabContainer" ).tabs({ 
			active: 0,
			disabled: [1]
     	});
     	
		$jQ( "#graphTabContainer" ).tabs({ active: 0 });

		<#-- submit button on tab 1 pressed -->
		$jQ( "#button_dsp-tab1" ).click( function()
		{
			// validate field values first
			//if ( validateDatapoint )
			//	if ( !validatationOfDatasetSucceed() )
			//		return false;
		
			changeTabCondition( 1 );
			
			<#--  submit the first form via ajax post and get the second tab content-->
			ajaxDspFrequenciesTabContent( "#form-dsp-tab1", "#dsp-tab2" );
		} );
		
		<#-- submit button on tab 2 pressed -->
		$jQ( "#dsp-tab2" ).on( "click", "#button_dsp-tab2", function(){
			
		} );
		
	}); // end of document ready
	
	<#-- 
	ajax call for saving user inputs in spss import Frequencies on first or second tab
	and get the second or third tab content via ajax
	@param selectorForm     : jquery selector for form submit
	@param selectorCallback : jquery selector for callback content from ajax call
	-->
	function ajaxDspFrequenciesTabContent( selectorForm, selectorCallback ){		
			$jQ.ajax( {
				type: "post",
				url: $jQ( selectorForm ).attr( 'action' ),
				data: $jQ( selectorForm ).serializeArray()
			    })
		    	.done( function( html ) {
		    		$jQ( selectorCallback ).html( html );
				})
		    	.fail( function() {})
		    	.always( function() {
		    		removeAjaxLoadingDiv();
		    	}); //end of ajax call
	}
	
	function changeTabCondition( tabIndex ){
	<#-- the pipeline of the process always move forward -->
		<#-- enable next tab -->
		$jQ( "#updateSpssTabContainer" ).tabs( "enable",  tabIndex );
		$jQ( "#updateSpssTabContainer" ).tabs( { active: tabIndex } );
     	<#-- set previously tab checked -->
     	$jQ( "#updateSpssTabContainer ul li:nth-child(" + tabIndex + ") a span" )
     	.html('<img src="<@spring.url '/resources/images/check_black.png' />" class="checkedTab">');
     	
	}
</script>