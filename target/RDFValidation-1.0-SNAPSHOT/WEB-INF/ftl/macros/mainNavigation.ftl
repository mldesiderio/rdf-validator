<!-- later this will be generated by the spring application, somewhere else? -->
<div id="nav_main">
	<ul class="homeButton" style="width: 225px">
		<li>
			<a href="http://www.gesis.org">
				<img alt="home button" src="<@spring.url '/resources/images/gs_home.png' />" />
			</a>
		</li>
	</ul>
	<ul class="MISSY_nav_main_marginFix">
	
		<li<#if link?? & link == 'home'> id="current"</#if>>
			<a href="<@spring.url '/home' />">Home</a>
		</li>

		<li<#if link?? & link == 'owl2'> id="current"</#if>>
			<a href="<@spring.url '/owl2' />">OWL 2</a>
		</li>
		
		<li<#if link?? & link == 'dsp'> id="current"</#if>>
			<a href="<@spring.url '/dsp' />">DSP</a>
		</li>
	</ul>
</div>