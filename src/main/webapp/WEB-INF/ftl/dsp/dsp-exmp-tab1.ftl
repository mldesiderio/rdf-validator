<form id="form-dsp-tab1" action="<@spring.url '/dsp/exmpgraph/tab1' />" style="padding-left: 25px" class="MISSY_round_right" >	

	<fieldset>
	
	  	<input type="checkbox" name="ad-hoc_module" onchange="unfoldToggle(this);" style="width:14px;margin:0px;"/> Unfold All
		<div id="tree" class="tree-container" style="width:100%">
			<img src="<@spring.url '/resources/images/loading.gif' />" /> loading...
		</div>
	  	
		<#-- form onsite help -->
		<a href="#" class="MISSY_onsiteHelp">
			<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_iconOnsitehelp" />
			<span>
				<img class="MISSY_onsiteHelpCallout" src="<@spring.url '/resources/images/onsiteHelpCallout.gif' />">
				<h4 class="MISSY_onsiteHelp">Select File</h4>
				<img src="<@spring.url '/resources/images/gs_icon.question_blue.png' />" class="MISSY_onsiteHelpHeaderIcon" /><br clear="all">
				Constraints
			</span>
		</a>
	  	
	</fieldset>
	
	<br />
	<hr />
	
	<fieldset>
		<input 
		  type="button" 
		  name="button_dsp-exmp-tab1" 
		  id="button_dsp-exmp-tab1" 
		  value="Next: Show File Content" 
		  class="buttonSubmit MISSY_loginSubmit" 
		  style="float: right; margin-top: 10px">
	</fieldset>
	
	<input type="hidden" id="filePath" name="filePath" />

</form>
<script>
	$jQ(document).ready(function() {
	
	    // check username availability on focus lost
	    $jQ( '#form-dsp-tab2' ).on( "focus", 'input[type="text"]', function (){
	    	if( $jQ(this).hasClass( "form-error" ) ){
	    		$jQ(this).removeClass( "form-error" );
	    		$jQ(this).next( "div.errormsg" ).remove();
	    	}
	    });
	    
	    <#-- create the tree -->
	    createTree();
	    <#-- disable button next -->
	    $jQ('#button_dsp-exmp-tab1').attr("disabled",true).addClass("buttonSubmitDissabled");
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
							 $jQ('#button_dsp-exmp-tab1').attr("disabled",true).addClass("buttonSubmitDissabled");
						 else{
						 	$jQ('#button_dsp-exmp-tab1').attr("disabled",false).removeClass("buttonSubmitDissabled");
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