<form id="form-dsp-tab1" action="<@spring.url '/dsp/onegraph/tab1' />" style="padding-left: 25px" class="MISSY_round_right">
	
	<fieldset>
	
		<!--
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
	    -->
	    
	    <ul style="margin: 0;">
	    	<li style="list-style-type: disc;">please select (multiple) files containing constraints and data</li>
	    	<li style="list-style-type: disc;">files must contain: constraints, data</li>
	    	<li style="list-style-type: disc;">files may contain: namespace declarations, inference rules</li>
	    	<li style="list-style-type: disc;">files must be written using W3C RDF turtle syntax</li>
	    </ul>
	    
		<table>
	        <tr style="background:transparent">
	            <td style="width:50%">
	            	<span style="margin-top:5px;">Multiple File Upload : </span>
	            	<input id="fileupload" style="width:60%;max-width:none" type="file" name="files[]" data-url="<@spring.url '/dsp/multiple-file-upload' />" multiple />
				</td>
				<td>
				    <#-- form onsite help -->
					<a href="#" class="MISSY_onsiteHelp" style="margin-top:6px">
						<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_iconOnsitehelp" />
						<span>
							<img class="MISSY_onsiteHelpCallout" src="<@spring.url '/resources/images/onsiteHelpCallout.gif' />">
							<h4 class="MISSY_onsiteHelp">Multiple File Upload</h4>
							<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_onsiteHelpHeaderIcon" /><br clear="all">
						    <ul style="margin: 0;">
						    	<li style="list-style-type: disc;">predefined namespace declarations: ...</li>
						    	<li style="list-style-type: disc;">please view the file contents by clicking on the file name below the browse button</li>
						    </ul>
						</span>
					</a>
				</td>
	            <td>
	            	<div id="progress" class="progress" style="width:70%;display:none">
				        <div class="bar" style="width: 0%;"></div>
				    </div>
				</td>
	        </tr>
	    </table>
	
	  	<div id="accordion_result_area">
	  		
	  	</div>
    
    	<!--
	  	<textarea name="rdfGraph" id="rdfGraph" cols="50" rows="20"></textarea>	
	  	-->
	  	
	</fieldset>
	  
	<br />
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
         	$jQ('#rdfGraph').val( data.result.fileContent );
        },
 
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $jQ('#progress .bar').css('width', progress + '%').html( progress + '%');
        	$jQ('#progress').show();
            if( progress == 100 )
            	window.setTimeout( function(){$jQ('#progress').fadeOut( "slow" ); } , 3000);
        }
    });
    
	$jQ(function() {
		<#-- populate uploaded files -->
    	getUploadedDocument();
    	
    	<#-- multiple file-upload -->
    	$jQ('#fileupload').fileupload({
	        dataType: 'json',
	 
	        done: function (e, data) {
 				printUploadedFiles( data.result );
	        },
	 
	        progressall: function (e, data) {
	            var progress = parseInt(data.loaded / data.total * 100, 10);
	            $jQ('#progress .bar').css('width', progress + '%').html( progress + '%');
	        	$jQ('#progress').show();
	            if( progress == 100 )
	            	window.setTimeout( function(){$jQ('#progress').fadeOut( "slow" ); } , 3000);
	        }
	    });
	});
	
	function getUploadedDocument(){
		$jQ.ajax({
		  	url: "<@spring.url '/dsp/getuploaded' />",
		  	dataType: 'json'
		}).done(function( data ) {
			if( data.length > 0 )
		 		printUploadedFiles( data );
		});
	}
	
	function printUploadedFiles( data ){
		$jQ("#accordion_result_area").html("");
		$jQ.each( data , function (index, file) {
	 
            $jQ("#accordion_result_area")
            .append( $jQ('<h3/>').text( "" + file.fileName).css('cursor', 'pointer').on("click", function(){ $jQ( this ).next().slideToggle() }) )
            .append(  $jQ('<div/>').append($jQ('<textarea/>').val(file.fileContent)).css('display', 'none') );
        }); 
	}
    
</script>