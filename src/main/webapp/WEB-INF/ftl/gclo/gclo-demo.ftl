<h1>Demo</h1>
<!-- the tabs -->
<div id="gcloDemoTabContainer" class="tabContainer">
	<ul style="height: 40px;">	
		<li><a class="tabElement" href="#gclo-demo-tab1" style="text-decoration:none;padding: 10px 15px;"><span>1.</span> Namespace Declarations</a></li>
		<li><a class="tabElement" href="#gclo-demo-tab2" style="text-decoration:none;padding: 10px 15px;"><span>2.</span> Constraints</a></li>
		<li><a class="tabElement" href="#gclo-demo-tab3" style="text-decoration:none;padding: 10px 15px;"><span>3.</span> Data</a></li>
		<!--<li><a class="tabElement" href="#gclo-demo-tab4" style="text-decoration:none;padding: 10px 15px;"><span>4.</span> Inference Rules</a></li>-->
		<li id="gclo-validation-tab"><a class="tabElement" href="#gclo-demo-tab5" style="text-decoration:none;padding: 10px 15px;"><span>4.</span> Validation</a></li>
	</ul>

	<div id="gclo-demo-tab1" style="padding:0 !important; border:none; float:left; width:100%">
		<#include "gclo-demo-tab1.ftl" />
	</div>
	<div id="gclo-demo-tab2" style="padding:0 !important;border:none;float:left; width:100%">
		<#include "gclo-demo-tab2.ftl" />
	</div>
	<div id="gclo-demo-tab3" style="padding:0 !important;border:none;float:left; width:100%">
		<#include "gclo-demo-tab3.ftl" />
	</div>
	<!--
	<div id="gclo-demo-tab4" style="padding:0 !important;border:none;float:left; width:100%">
		<#include "gclo-demo-tab4.ftl" />
	</div>
	-->
	<div id="gclo-demo-tab5" style="padding:0 !important;border:none;float:left; width:100%">
		<#include "gclo-demo-tab5.ftl" />
	</div>


<script>
	$jQ( function() // begin document ready
	{
		<#-- change the size of main content -->
		$jQ( "#col3" ).css( "margin", "0 0 0 15px");
	
		<#-- jQery tab for main content 'define study, define spss file, import -->
		$jQ( "#gcloDemoTabContainer" ).tabs({ 
			active: 0
			<!-- disabled: [1,2,3] -->
     	});
     	
     	<#-- on last tab press - collect all inputs -->
     	$jQ( "#gclo-validation-tab" ).click( function(){
     		$jQ("pre#namespaceDeclarationsPre").text( $jQ( "#containerNamespaceDeclarations" ).find( "textarea" ).val() );
     		$jQ("pre#constraintsPre").text( $jQ( "#containerConstraints" ).find( "textarea" ).val() );
     		$jQ("pre#dataPre").text( $jQ( "#containerData" ).find( "textarea" ).val() );
     		$jQ("pre#inferenceRulesPre").text( $jQ( "#containerInferenceRules" ).find( "textarea" ).val() );
     	});
     	
     	<#-- on button validate press - sent inputs via ajax and get the results  -->
     	$jQ( "#button_gclo-demo-validation" ).click( function(){
     		<#-- validate the result -->
     		ajaxGcloDemoValidation( "#gclo-validation-result" );
     	});
  
		
	}); // end of document ready
	
	<#-- 
	ajax call for validates user input
	-->
	function ajaxGcloDemoValidation( containerResult ){
		<#-- disable validate button -->
		$jQ('#button_gclo-demo-validation').attr("disabled",true).addClass("buttonSubmitDissabled");
		<#-- Add loading icon -->
		$jQ( containerResult )
			.html( "" )
			.append( "<h3>Constraint Violations</h3> " )
			.append( '<div style="width:100%;text-align:center"><img src="<@spring.url '/resources/images/ajaxLoader.gif' />" /></div>' );

		
		$jQ.ajax( {
			type: "post",
			url: "<@spring.url '/gclo/demo/validation' />",
			data: {
				nameSpaceDeclaration : $jQ( "#containerNamespaceDeclarations" ).find( "textarea" ).val(),
				constraints : $jQ( "#containerConstraints" ).find( "textarea" ).val(),
				data : $jQ( "#containerData" ).find( "textarea" ).val(),
				inferenceRules : $jQ( "#containerInferenceRules" ).find( "textarea" ).val()
				}
		    })
	    	.done( function( html ) {
	    		$jQ( containerResult ).html( html );
	    		<#-- enable validate button -->
				$jQ('#button_gclo-demo-validation').attr("disabled",false).removeClass("buttonSubmitDissabled");
			})
	    	.fail( function() {})
	    	.always( function() {
	    		//removeAjaxLoadingDiv();
	    	}); //end of ajax call
	}
	
</script>