<#if spinException??>

	<h3>Validation Error</h3> 
	
		<br/>
	
		<table class="datatable">
				<tr>
					<td><b>source</b></td>
					<td>${spinException.getSource()}</td>
				</tr>
				<tr>
					<td><b>message</b></td>
					<td>${spinException.getMessage()}</td>
				</tr>
	  	    	<tr><td><br/></td><td><br/></td></tr>
		</table>
		
	<hr/>
	<br/>

<#else>
</#if>

<h3>Constraint Violations</h3> 
	<br/>

	<!--
	<#if discoValidationResult??>${discoValidationResult}<#else></#if>
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
					<td><#if constraintViolation.root??>${constraintViolation.root}<#else></#if></td>
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
	  	    	    <#if constraintViolationFix??>
			  	    	<tr>
							<td><b>fix</b></td>
							<td>${constraintViolationFix}</td>
						</tr>
			  	    	<#else>
			  	    	</#if>
	  	    	</#list>
	  	    	<#if constraintViolation.severityLevel??>
	  	    	<#if constraintViolation.severityLevel == 'INFO'>
					<tr>
						<td><span style="color:blue"><b>severity</b></span></td>
						<td><span style="color:blue">${constraintViolation.severityLevel}</span></td>
					</tr>
				</#if>  
	  	    	<#if constraintViolation.severityLevel == 'WARNING'>
					<tr>
						<td><span style="color:orange"><b>severity</b></span></td>
						<td><span style="color:orange">${constraintViolation.severityLevel}</span></td>
					</tr>
				</#if>  
				<#if constraintViolation.severityLevel == 'ERROR'>
					<tr>
						<td><span style="color:red"><b>severity</b></span></td>
						<td><span style="color:red">${constraintViolation.severityLevel}</span></td>
					</tr>
				</#if>  
	  	    	<#else>
	  	    	</#if>
	  	    	<!--
	  	    	<tr>
					<td><b>severity</b></td>
					<td><#if constraintViolation.severityLevel??>${constraintViolation.severityLevel}<#else></#if></td>
				</tr>
				-->
	  	    	<tr><td><br/></td><td><br/></td></tr>
	    	</#list>
		</table>
	  
	<#else>
	</#if>
	
<hr/>
<br/>