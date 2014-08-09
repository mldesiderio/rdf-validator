<form id="form-dsp-tab1" action="<@spring.url '/dsp/ngraph/tab1' />" style="padding-left: 25px" class="MISSY_round_right">

	<fieldset>
	
		<ul style="margin: 0;">
	    	<li style="list-style-type: disc;">predefined namespace declarations are added automatically</li>
	    	<li style="list-style-type: disc;">you may add additional namespace declarations</li>
	    	<li style="list-style-type: disc;">namespace declarations must be written in W3C RDF turtle syntax</li>
	    </ul>
	    
	    <hr/>
	    
		<ul style="margin: 0;">
	    	<li style="list-style-type: disc;">you may upload a file containing namespace declarations</li>
	    </ul>
	
		<table>
	        <tr style="background:transparent">
	            <td style="width:50%">
	            	<input id="fileupload" style="width:100%;max-width:none" type="file" name="files[]" data-url="<@spring.url '/dsp/upload' />" multiple />
				</td>
	            <td>
	            	<div id="progress" class="progress" style="width:70%;display:none">
				        <div class="bar" style="width: 0%;"></div>
				    </div>
				</td>
	        </tr>
	    </table>
	    
	    <hr/>
    
	  	<textarea name="namespaceDeclarations" id="namespaceDeclarations" cols="50" rows="20" style="height:250px"></textarea>	
	  
		<#-- form onsite help -->
		<a href="#" class="MISSY_onsiteHelp" style="margin-top:0px;vertical-align:top;">
			<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_iconOnsitehelp" />
			<span style="width:350px;">
				<img class="MISSY_onsiteHelpCallout" src="<@spring.url '/resources/images/onsiteHelpCallout.gif' />">
				<h4 class="MISSY_onsiteHelp">Namespace Declarations</h4>
				<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_onsiteHelpHeaderIcon" /><br clear="all">
			    <ul style="margin: 0;">
			    	<li style="list-style-type: disc;">predefined namespace declarations: xsd, rdf, rdfs, owl, foaf, dcterms, skos, dcam, dsp, : (default namespace)</li>
			    	<li style="list-style-type: disc;">you may adjust the height and the width of the textarea by dragging the small arrow at the right end of the textarea</li>
			    </ul>
			</span>
		</a>
	  	
	</fieldset>
	  
	<hr />
	
	<fieldset>
		<input 
		  type="button" 
		  name="button_dsp-tab1" 
		  id="button_dsp-tab1" 
		  value="Next: Constraints" 
		  class="buttonSubmit MISSY_loginSubmit" 
		  style="float: right; margin-top: 10px">
	</fieldset>
	
</form>
<script>
	$jQ('#fileupload').fileupload({
        dataType: 'json',
 
        done: function (e, data) {
         	$jQ('#namespaceDeclarations').val( data.result.fileContent );
        },
 
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $jQ('#progress .bar').css('width', progress + '%').html( progress + '%');
        	$jQ('#progress').show();
            if( progress == 100 )
            	window.setTimeout( function(){$jQ('#progress').fadeOut( "slow" ); } , 3000);
        }
    });
</script>