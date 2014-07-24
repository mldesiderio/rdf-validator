<form id="form-dsp-tab1" action="<@spring.url '/dsp/tab1' />" style="padding-left: 25px" class="MISSY_round_right">

	<fieldset>
	
		<table>
	        <tr style="background:transparent">
	            <td style="width:50%">
	            	<input id="fileupload" style="width:100%;max-width:none" type="file" name="files[]" data-url="<@spring.url '/dsp/upload' />" multiple />
				</td>
	            <td>
	            	<div id="progress" class="progress" style="width:70%;margin-top:5px">
				        <div class="bar" style="width: 0%;"></div>
				    </div>
				</td>
	        </tr>
	    </table>
    
	  	<textarea name="namespaceDeclarations" id="namespaceDeclarations" cols="50" rows="20"></textarea>	
	  
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
            $jQ.each(data.result, function (index, file) {
                 $jQ('#namespaceDeclarations').val(
                	'file name : ' + file.fileName + '\n' +
                	'file size : ' + file.fileSize + '\n' +
                	'file type : ' + file.fileType + '\n' +
                	'file content : \n' + file.fileContent
                );
            }); 
        },
 
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $jQ('#progress .bar').css(
                'width',
                progress + '%'
            );
        }
    });
</script>