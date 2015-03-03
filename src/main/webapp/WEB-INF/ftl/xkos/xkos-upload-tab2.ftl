<form id="form-xkos-tab5" action="<@spring.url '/xkos/onegraph/tab5' />" style="padding-left: 25px" class="MISSY_round_right">

	<fieldset>
		<input 
		  type="button" 
		  name="button_xkos-upload-validation" 
		  id="button_xkos-upload-validation" 
		  value="Validate" 
		  class="buttonSubmit MISSY_loginSubmit" 
		  style="float: right; margin-top: 10px">
	</fieldset>
	
	<fieldset id="xkos-validation-result">
		<#-- ajax content here -->
	</fieldset>

	
	<fieldset id="xkos-validation-inputs">
		
		<h3>Input RDF Graphs</h3> 
		<br/>
		
		<!--
		<pre><#if rdfGraph??>${rdfGraph}<#else></#if></pre>
		-->
		<div>
		<#if fileInputGraphList??>
			
			<#list fileInputGraphList as fileInputGraph>
				<table class="datatable">
				    <tr>
				    	<td>file</td>
				        <td><#if fileInputGraph.filename??>${fileInputGraph.filename}<#else></#if></td>
				    </tr>
				</table>
				<table class="datatable">
				    <tr>
				    	<td>input graph</td>
				        <td><pre><#if fileInputGraph.inputGraph??>${fileInputGraph.inputGraph}<#else></#if></pre></td>
				    </tr>
				</table>
				<br/>
				<hr/>
				<br/>
			</#list> 
		  
		<#else>
		</#if>
		</div>

	</fieldset>
	
	
</form>