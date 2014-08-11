<form style="padding-left: 25px"  id="form-dsp-demo-tab3" action="<@spring.url '/dsp/demo/tab3' />" class="MISSY_round_right">

	
	<fieldset>
	
		<ul style="margin: 0;">
	    	<li style="list-style-type: disc;">
	    	please enter your data
	    	
	    	<#-- form onsite help -->
			<a href="#" class="MISSY_onsiteHelp" style="margin:0 0 0 80px;vertical-align:top;">
				<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_iconOnsitehelp" />
				<span style="width:350px;">
					<img class="MISSY_onsiteHelpCallout" src="<@spring.url '/resources/images/onsiteHelpCallout.gif' />">
					<h4 class="MISSY_onsiteHelp">Data</h4>
					<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_onsiteHelpHeaderIcon" /><br clear="all">
				    <ul style="margin: 0;">
				    	<li style="list-style-type: disc;">you may adjust the height and the width of the textarea by dragging the small arrow at the right end of the textarea</li>
				    </ul>
				</span>
			</a>
	    	
	    	</li>
	    	<li style="list-style-type: disc;">please use W3C RDF turtle syntax</li>
	    </ul>
	    
	    <hr/>
	
	  	<div id="containerData"></div>	 
	  	<input type="hidden" name="data" id="data" />
	 
	</fieldset>
	
	<hr />
	
	<fieldset>
		<input 
		  type="button" 
		  name="button_dsp-demo-tab3" 
		  id="button_dsp-demo-tab3" 
		  value="Next: Validation Results" 
		  class="buttonSubmit MISSY_loginSubmit" 
		  style="float: right; margin-top: 10px">
	</fieldset>
	
</form>

<script>
  	$jQ(function() {
  	
  		<#-- show empty textarea -->
	    createRdfOwlView( "#containerData" , ""  , [] );
	    
    	$jQ("#text_output")
		  .wrap('<div/>')
		    .css({'overflow':'hidden'})
		      .parent()
		        .css({'display':'inline-block',
		              'overflow':'hidden',
		              'height':function(){return $jQ('#text_output',this).height();},
		              'width': '100%',
		              'paddingBottom':'12px',
		              'paddingRight':'12px'
		
		             }).resizable({
		             	grid: [10000, 1]
		             })
		                .find('#text_output')
		                  .css({overflow:'auto',
		                        width:'100%',
		                        height:'100%'});
		  	});
		  	
	$jQ('#fileupload3').fileupload({
        dataType: 'json',
 
        done: function (e, data) {
         	$jQ('#data').val( data.result.fileContent );
        },
 
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $jQ('#progress3 .bar').css('width', progress + '%').html( progress + '%');
        	$jQ('#progress3').show();
            if( progress == 100 )
            	window.setTimeout( function(){$jQ('#progress3').fadeOut( "slow" ); } , 3000);
        }
    });
 </script>