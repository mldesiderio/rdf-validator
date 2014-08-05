<form id="form-owl2-tab1" action="<@spring.url '/owl2/tab1' />" style="padding-left: 25px" class="MISSY_round_right">

	<fieldset>
	
		<span> Select files : </span>
		<select id="resource_files">
			<option value="">Select One</option>
		</select>
	
	  	<textarea id="namespaceDeclarations" name="namespaceDeclarations" cols="50" rows="20"></textarea>	
	  
		<#-- form onsite help -->
		<a href="#" class="MISSY_onsiteHelp" style="margin-top:-20px">
			<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_iconOnsitehelp" />
			<span>
				<img class="MISSY_onsiteHelpCallout" src="<@spring.url '/resources/images/onsiteHelpCallout.gif' />">
				<h4 class="MISSY_onsiteHelp">Namespace Declarations</h4>
				<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_onsiteHelpHeaderIcon" /><br clear="all">
				Namespace Declarations
			</span>
		</a>
	  	
	</fieldset>
	  
	<br />
	<hr />
	
	<fieldset>
		<input 
		  type="button" 
		  name="button_owl2-tab1" 
		  id="button_owl2-tab1" 
		  value="Next: Constraints" 
		  class="buttonSubmit MISSY_loginSubmit" 
		  style="float: right; margin-top: 10px">
	</fieldset>
	
</form>

<script>
	$jQ(document).ready(function() {
	    <#-- create the tree -->
	    populateComboBox();
	    <#-- disable button next -->
	    <#--$jQ('#button_dsp-exmp-tab1').attr("disabled",true).addClass("buttonSubmitDissabled");-->
	    
	    <#-- add event onclick for select option -->
	     $jQ('#resource_files').on( "click", function(){
	     	if( $jQ( this ).val() != "" ){
	     		$jQ.ajax( {
					type: "post",
					url:  "<@spring.url '/owl2/file_details' />",
					data: { filePath : $jQ( this ).val() }
				    })
			    	.done( function( data ) {
			    		$jQ( "#namespaceDeclarations" ).val( data.fileContent );
					})
			    	.fail( function() {})
			    	.always( function() {
			    		removeAjaxLoadingDiv();
			    	}); //end of ajax call
	     	}
	     });
	});
	
	function populateComboBox(){
		$jQ.getJSON( "<@spring.url '/owl2/resource_structure' />" , function( data ) {
					
			<#-- add the options for the combobox -->
			$jQ.each(data, function(index, item) {   
			     $jQ('#resource_files')
			         .append($jQ("<option></option>")
			         .attr("value",item.key)
			         .text(item.title)); 
			});

		}).fail(function(){}).always(function(){});
	}

</script>
