<form id="form-dsp-demo-tab1" action="<@spring.url '/dsp/demo/tab1' />" style="padding-left: 25px" class="MISSY_round_right" >	

	<fieldset>
	
	    <ul style="margin: 0;">
	    	<li style="list-style-type: disc;">predefined namespace declarations are added automatically</li>
	    	<li style="list-style-type: disc;">please add additional namespace declarations</li>
	    </ul>
	    
	    <hr/>
	
	  	<textarea name="namespaceDeclarations" id="namespaceDeclarations" cols="50" rows="20"></textarea>	
	  	
		<#-- form onsite help -->
		<a href="#" class="MISSY_onsiteHelp" style="margin-top:0px;vertical-align:top;">
			<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_iconOnsitehelp" />
			<span style="width:250px;">
				<img class="MISSY_onsiteHelpCallout" src="<@spring.url '/resources/images/onsiteHelpCallout.gif' />">
				<h4 class="MISSY_onsiteHelp">Namespace Declarations</h4>
				<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_onsiteHelpHeaderIcon" /><br clear="all">
			    <ul style="margin: 0;">
			    	<li style="list-style-type: disc;">predefined namespace declarations: ...</li>
			    </ul>
			</span>
		</a>
	  	
	</fieldset>
	
	<br />
	<hr />
	
	<fieldset>
		<input 
		  type="button" 
		  name="button_dsp-demo-tab1" 
		  id="button_dsp-demo-tab1" 
		  value="Next: Constraints" 
		  class="buttonSubmit MISSY_loginSubmit" 
		  style="float: right; margin-top: 10px">
	</fieldset>

</form>

<script>
	$jQ('#fileupload2').fileupload({
        dataType: 'json',
 
        done: function (e, data) {
         	$jQ('#constraints').val( data.result.fileContent );
        },
 
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $jQ('#progress2 .bar').css('width', progress + '%').html( progress + '%');
        	$jQ('#progress2').show();
            if( progress == 100 )
            	window.setTimeout( function(){$jQ('#progress2').fadeOut( "slow" ); } , 3000);
        }
    });
</script>