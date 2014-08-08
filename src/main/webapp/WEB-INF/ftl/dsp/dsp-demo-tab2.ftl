<form id="form-dsp-demo-tab2" action="<@spring.url '/dsp/demo/tab2' />" style="padding-left: 25px" class="MISSY_round_right" >	

	<fieldset>
	
		<ul style="margin: 0;">
	    	<li style="list-style-type: disc;">please enter your constraints</li>
	    	<li style="list-style-type: disc;">please use W3C RDF turtle syntax</li>
	    	<li style="list-style-type: disc;">you may also select a file containing example constraints (see below)</li>
	    </ul>
	    
	    <hr/>
	
		<textarea name="constraints" id="constraints" cols="50" rows="20" style="height:150px"></textarea>	
		
		<#-- form onsite help -->
		<a href="#" class="MISSY_onsiteHelp" style="margin-top:0px;vertical-align:top;">
			<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_iconOnsitehelp" />
			<span style="width:350px;">
				<img class="MISSY_onsiteHelpCallout" src="<@spring.url '/resources/images/onsiteHelpCallout.gif' />">
				<h4 class="MISSY_onsiteHelp">Namespace Declarations</h4>
				<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_onsiteHelpHeaderIcon" /><br clear="all">
			    <ul style="margin: 0;">
			    	<li style="list-style-type: disc;">you may adjust the height and the width of the textarea by dragging the small arrow at the right end of the textarea</li>
			    </ul>
			</span>
		</a>
	  	
	</fieldset>

	<hr/>

	<fieldset>
	
		<ul style="margin: 0;">
	    	<li style="list-style-type: disc;">you may select a file containing example constraints</li>
	    </ul>
	    
	    <hr/>
	
	  	<input type="checkbox" name="ad-hoc_module" onchange="unfoldToggle(this);" style="width:14px;margin:0px;"/> Unfold All
		<div id="tree" class="tree-container" style="width:100%">
			<img src="<@spring.url '/resources/images/loading.gif' />" /> loading...
			
		</div>
	  	
		<#-- form onsite help -->
		<a href="#" class="MISSY_onsiteHelp" style="margin-top:0px;vertical-align:top;">
			<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_iconOnsitehelp" />
			<span style="width:350px;">
				<img class="MISSY_onsiteHelpCallout" src="<@spring.url '/resources/images/onsiteHelpCallout.gif' />">
				<h4 class="MISSY_onsiteHelp">Namespace Declarations</h4>
				<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_onsiteHelpHeaderIcon" /><br clear="all">
			    <ul style="margin: 0;">
			    	<li style="list-style-type: disc;">you may select a file containing example constraints</li>
			    </ul>
			</span>
		</a>
	  	
	</fieldset>
	
	<br />
	<hr />
	
	<fieldset>
		<input 
		  type="button" 
		  name="button_dsp-demo-tab2" 
		  id="button_dsp-demo-tab2" 
		  value="Next: Data" 
		  class="buttonSubmit MISSY_loginSubmit" 
		  style="float: right; margin-top: 10px">
	</fieldset>
	
	<input type="hidden" id="filePath" name="filePath" />

</form>
<script>
	$jQ(document).ready(function() {
	
	    // check username availability on focus lost
	    $jQ( '#form-dsp-demo-tab2' ).on( "focus", 'input[type="text"]', function (){
	    	if( $jQ(this).hasClass( "form-error" ) ){
	    		$jQ(this).removeClass( "form-error" );
	    		$jQ(this).next( "div.errormsg" ).remove();
	    	}
	    });
	    
	    <#-- create the tree -->
	    createTree();
	    <#-- disable button next -->
	    <#-- $jQ('#button_dsp-demo-tab2').attr("disabled",true).addClass("buttonSubmitDissabled"); -->
	});
	
	function createTree(){
		$jQ.getJSON( "<@spring.url '/dsp/resource_structure' />" , function( data ) {
					
				<#-- create the tree after response from the server (tree structure of ul li) available -->
				$jQ('#tree').dynatree( 
				{
					selectMode: 2,
					children: data,
					onActivate: function(node) {
						if(node.data.isFolder)
							 $jQ('#button_dsp-demo-tab2').attr("disabled",true).addClass("buttonSubmitDissabled");
						 else{
						 	$jQ('#button_dsp-demo-tab2').attr("disabled",false).removeClass("buttonSubmitDissabled");
						 	$jQ( "#filePath" ).val( node.data.url );
					 	}
		          	}
		         });

		}).fail(function(){}).always(function(){});
	}
	
	//toggle tree shrink and collapse when checkbox is checked
    function unfoldToggle(checkbox){
        if (checkbox.checked == true) {
            $jQ("#tree").dynatree("getRoot").visit(function(node){
                node.expand(true);
            });
            return false;
        } else {
            $jQ("#tree").dynatree("getRoot").visit(function(node){
                node.expand(false);
            });
            return false;
        }
    }
</script>