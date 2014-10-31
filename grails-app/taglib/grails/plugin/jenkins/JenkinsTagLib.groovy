package grails.plugin.jenkins

class JenkinsTagLib {
	static namespace = "jen"
	def grailsApplication
	def jenkinsService
	// Logics to produce requirements put into taglib, controller page loads in taglib
	// saves writing twice and now end user can interact directly via taglib calls
	def connect =  { attrs, body ->
		def template=attrs.remove('template')?.toString()
		def divId=attrs.remove('divId')?.toString()
		def jensuser=attrs.remove('jensuser')?.toString()
		def jenspass=attrs.remove('jenspass')?.toString()
		def jenschoice=attrs.remove('jenschoice')?.toString()


		//def jensprefix=attrs.remove('jensprefix')?.toString()
		if (!attrs.jensjob) {
			throwTagError("Tag [jenkins] is missing required attribute [jensjob]")
		}

		// websocket url
		def wshostname=attrs.wshostname ?: grailsApplication.config.jenkins.wshostname ?: 'localhost:8080'

		String jenserver=attrs.jenserver ?: 'localhost'

		//jenkins server
		StringBuilder jenurl=new StringBuilder()
		if (!jenserver.startsWith('http')) {
			jenurl.append('http://'+jenserver)
		}else {
			jenurl.append(jenserver)
		}

		// If you defined a port append to url
		if (attrs.jensport) {
			jenurl.append(':'+attrs.jensport)
		}
		// Full url to jenkins server - main url
		String jenfullserver=jenurl.toString()
		String jensprefix=''
		if (attrs.jensprefix) {
			jensprefix=jenkinsService.seperator(attrs?.jensprefix)
			jenurl.append(jensprefix)
		}
		// Full url to jenkins server - main url
		String jensconurl=jenurl.toString()

		//String jensjob=attrs.jensjob
		//String jensfolder=attrs.jensfolder ?: 'job'
		// Relative UrI to get to folder/job (can now be appended to above)
		String jensurl=jensprefix+jenkinsService.seperator(attrs?.jensfolder)+jenkinsService.seperator(attrs?.jensjob)

		// Configuration uris.
		//Progressive page
		// Default: '/logText/progressiveHtml'
		String jensprogressive=attrs.jensprogressive ?: grailsApplication.config.jenkins.progressiveuri ?: '/logText/progressiveHtml'


		//Ending uri for when building
		// Default: '/build?delay=0sec'
		String jensbuildend=attrs.jensbuildend ?: grailsApplication.config.jenkins.buildend ?:  '/build?delay=0sec'

		// jens uri to get to full logs:
		// Default: '/consoleFull'
		String jensconlog=attrs.jensLog ?: grailsApplication.config.jenkins.consoleLog ?: '/consoleFull'


		String hideButtons=attrs.hideButtons ?: grailsApplication.config.jenkins.hideButtons ?: 'no'
		String hideTriggerButton=attrs.hideTriggerButton ?: grailsApplication.config.jenkins.hideTriggerButton ?: 'no'
		String hideDashBoardButton=attrs.hideDashBoardButton ?: grailsApplication.config.jenkins.hideDashBoardButton ?: 'no'
		// String hideBuildTimer=attrs.hideBuildTimer ?: grailsApplication.config.jenkins.hideBuildTimer ?: 'no'


		//println "--->> ${jenfullserver} --- ${jensconurl} "
		String appname=grailsApplication.metadata['app.name']
		if (template) {
			out << g.render(template:template, model: [hideButtons:hideButtons,hideTriggerButton:hideTriggerButton, hideDashBoardButton:hideDashBoardButton, jenschoice:jenschoice, divId:divId,jenfullserver:jenfullserver,jensconurl:jensconurl, jensjob:attrs.jensjob, jensuser:jensuser,jenspass:jenspass,appname:appname,wshostname:wshostname,jenserver:jenserver,jensurl:jensurl,jensprogressive:jensprogressive, jensbuildend:jensbuildend, jensconlog:jensconlog])
		}else{
			out << g.render(contextPath: pluginContextPath, template : '/jenkins/process', model: [hideButtons:hideButtons,hideTriggerButton:hideTriggerButton, hideDashBoardButton:hideDashBoardButton, jenschoice:jenschoice, divId:divId,jenfullserver:jenfullserver,jensconurl:jensconurl, jensjob:attrs.jensjob, jensuser:jensuser,jenspass:jenspass,appname:appname,wshostname:wshostname,jenserver:jenserver,jensurl:jensurl,jensprogressive:jensprogressive, jensbuildend:jensbuildend, jensconlog:jensconlog])
		}
	}


	def dirconnect= {attrs, body ->
		def template=attrs.remove('template')?.toString()
		def divId=attrs.remove('divId')?.toString()
		def jensuser=attrs.remove('jensuser')?.toString()
		def jenspass=attrs.remove('jenspass')?.toString()
		def jenschoice=attrs.remove('jenschoice')?.toString()
		def jensurl=attrs.remove('jensurl')?.toString()
		def url = new URL(jensurl)
		String jenserver=url.host
		String jensauthority=url.authority
		String jenspath=url.path
		String jensprot=url.protocol
		//String jensport=url.port

		String jensconurl=jensprot+'://'+jensauthority

		if (!attrs.jensjob) {
			throwTagError("Tag [jenkins] is missing required attribute [jensjob]")
		}
		def wshostname=attrs.wshostname ?: grailsApplication.config.jenkins.wshostname ?: 'localhost:8080'
		String jensprogressive=attrs.jensprogressive ?: grailsApplication.config.jenkins.progressiveuri ?: '/logText/progressiveHtml'
		String jensbuildend=attrs.jensbuildend ?: grailsApplication.config.jenkins.buildend ?:  '/build?delay=0sec'
		String jensconlog=attrs.jensLog ?: grailsApplication.config.jenkins.consoleLog ?: '/consoleFull'
		String hideButtons=attrs.hideButtons ?: grailsApplication.config.jenkins.hideButtons ?: 'no'
		String hideTriggerButton=attrs.hideTriggerButton ?: grailsApplication.config.jenkins.hideTriggerButton ?: 'no'
		String hideDashBoardButton=attrs.hideDashBoardButton ?: grailsApplication.config.jenkins.hideDashBoardButton ?: 'no'
		String appname=grailsApplication.metadata['app.name']
		def validurl=jenkinsService.verifyUrl(jensurl,jenserver,jensuser,jenspass)
		if (validurl.toString().startsWith('Success')) {
			if (template) {
				out << g.render(template:template, model: [hideButtons:hideButtons,hideTriggerButton:hideTriggerButton, hideDashBoardButton:hideDashBoardButton, jenschoice:jenschoice, divId:divId,jenfullserver:jensconurl,jensconurl:jensconurl, jensjob:attrs.jensjob, jensuser:jensuser,jenspass:jenspass,appname:appname,wshostname:wshostname,jenserver:jenserver,jensurl:jenspath,jensprogressive:jensprogressive, jensbuildend:jensbuildend, jensconlog:jensconlog])
			}else{
				out << g.render(contextPath: pluginContextPath, template : '/jenkins/process', model: [hideButtons:hideButtons,hideTriggerButton:hideTriggerButton, hideDashBoardButton:hideDashBoardButton, jenschoice:jenschoice, divId:divId,jenfullserver:jensconurl,jensconurl:jensconurl, jensjob:attrs.jensjob, jensuser:jensuser,jenspass:jenspass,appname:appname,wshostname:wshostname,jenserver:jenserver,jensurl:jenspath,jensprogressive:jensprogressive, jensbuildend:jensbuildend, jensconlog:jensconlog])
			}
		}
	}



}
