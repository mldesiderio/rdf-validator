<form id="form-dsp-demo-tab4" action="<@spring.url '/dsp/demo/tab4' />" style="padding-left: 25px" class="MISSY_round_right">

	<fieldset>
		<input 
		  type="button" 
		  name="button_dsp-demo-tab4" 
		  id="button_dsp-demo-tab4" 
		  value="Validate" 
		  class="buttonSubmit MISSY_loginSubmit" 
		  style="float: right; margin-top: 10px">
	</fieldset>
	
	<br />
	<hr />

	<fieldset>
	
		<h3>Constraint Violations</h3> 
		<br/>

		<!--
		<#if dspValidationResult??>${dspValidationResult}<#else></#if>
		-->
		
		<!-- debugging -->
		<!--
		<#if debugging??>${debugging}<#else></#if>
		-->

		<#if constraintViolationList??>
		  
			<table class="datatable">
				<#list constraintViolationList as constraintViolation>
					<tr>
						<td><b>root</b></td>
						<td>${constraintViolation.root}</td>
					</tr>
					<tr>
						<td><b>message</b></td>
						<td>${constraintViolation.message}</td>
					</tr>
					<tr>
						<td><b>source</b></td>
						<td>${constraintViolation.source}</td>
					</tr>
					<#list constraintViolation.paths as constraintViolationPath>
						<tr>
							<td><b>path</b></td>
							<td>${constraintViolationPath}</td>
						</tr>
		  	    	</#list>
		  	    	<#list constraintViolation.fixes as constraintViolationFix>
						<tr>
							<td><b>fix</b></td>
							<td>${constraintViolationFix}</td>
						</tr>
		  	    	</#list>
		  	    	<tr><td><br/></td><td><br/></td></tr>
		    	</#list>
			</table>
		  
		<#else>
		</#if>
		
	</fieldset>
	
	<hr/>
	<br/>
	
	<fieldset>
		
		<h3>Input RDF Graphs</h3> 
		<br/>
		
		<table>
			<th>Namespace Declarations</th>
		    <tr>
		        <td><pre><#if namespaceDeclarations??>${namespaceDeclarations}<#else></#if></pre></td>
		    </tr>
		</table>  
		
		<br/>
		
		<table>
			<th>Constraints</th>
		    <tr>
		        <td><pre><#if constraints??>${constraints}<#else></#if></pre></td>
		    </tr>
		</table>  
		
		<br/>
		
		<table>
			<th>Data</th>
		    <tr>
		        <td><pre><#if data??>${data}<#else></#if></pre></td>
		    </tr>
		</table>  
		
		<br/>
		
		<table>
			<th>Inference Rules</th>
		    <tr>
		        <td><pre><#if inferenceRules??>${inferenceRules}<#else></#if></pre></td>
		    </tr>
		</table>
		
		<br/> 

	</fieldset>
	
</form>