<form id="form-owl2-tab2" action="<@spring.url '/owl2/tab2' />" style="padding-left: 25px" class="MISSY_round_right" >	

	<fieldset>
	
		<table>
	        <tr style="background:transparent">
	            <td style="width:70%">
	            	<span style="margin-top:5px;">Multiple File Upload : </span>
	            	<input id="fileupload" style="width:60%;max-width:none" type="file" name="files[]" data-url="<@spring.url '/owl2/upload' />" multiple />
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
	  	
	</fieldset>
	
	<br />
	<hr />
	
	<fieldset>
		<input 
		  type="button" 
		  name="button_owl2-tab2" 
		  id="button_owl2-tab2" 
		  value="Next: Data" 
		  class="buttonSubmit MISSY_loginSubmit" 
		  style="float: right; margin-top: 10px">
	</fieldset>

</form>
<script>
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
		  	url: "<@spring.url '/owl2/getuploaded' />",
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
            .append( $jQ('<h3/>').text( "- " + file.fileName).css('cursor', 'pointer').on("click", function(){ $jQ( this ).next().slideToggle() }) )
            .append(  $jQ('<div/>').append($jQ('<textarea/>').val(file.fileContent)).css('display', 'none') );
        }); 
	}
	
	
</script>