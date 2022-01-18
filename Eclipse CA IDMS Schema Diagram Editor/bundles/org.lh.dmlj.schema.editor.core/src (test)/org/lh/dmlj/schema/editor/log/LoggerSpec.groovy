/**
 * Copyright (C) 2021  Luc Hermans
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.log

import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
 
import org.eclipse.core.runtime.ILog
import org.osgi.framework.Bundle

import spock.lang.Specification
import spock.lang.Unroll

class LoggerSpec extends Specification {
	
	def "Invoking the static factory method with a null argument creates a Logger that writes to a dummy log"() {
		
		when:
		Logger logger = Logger.getLogger(null)
		
		then:
		logger
		
		and:
		logger.bundleSymbolicName == '<no bundle symbolic name available>'
		logger.getLogProvider() instanceof DummyLogProvidingPlugin		
	}
	
	@Unroll("Invoking the static factory method with a LogProvidingPlugin creates a Logger with conditional debug message writing: #label")
	def "Invoking the static factory method with a LogProvidingPlugin creates a Logger with conditional debug message writing"() {		
		
		given:
		LogProvidingPlugin plugin = Mock(LogProvidingPlugin) {
			if (bundle) {
				getBundle() >> Mock(Bundle) {					
					getSymbolicName() >> bundleSymbolicName
					0 * _
				}
			} else {
				getBundle() >> null
			}
			getLog() >> null
			0 * _
		}
		
		when:
		Logger logger = Logger.getLogger(plugin)
		
		then:
		logger
		
		and:
		logger.bundleSymbolicName == expectedBundleSymbolicName
		logger.getLogProvider().is plugin
		
		where:
		label 						   | bundle | bundleSymbolicName	 || expectedBundleSymbolicName
		'no bundle' 				   | false  | null					 || '<no bundle symbolic name available>'
		'bundle without symbolic name' | true	| null					 || '<no bundle symbolic name available>'
		'bundle with symbolic name'    | true   | 'bundle.symbolic.name' || 'bundle.symbolic.name'
	}
	
	def "The Logger always writes informational messages"() {
		
		given:
		ILog log = Mock(ILog)
		
		and:	
		LogProvidingPlugin plugin = Mock(LogProvidingPlugin) {
			getBundle() >> Mock(Bundle) {					
				getSymbolicName() >> 'bundle.symbolic.name'
				0 * _
			}
			getLog() >> log
			0 * _
		}
		
		and:
		Logger logger = Logger.getLogger(plugin)
		
		when:
		logger.info('informational message')
		
		then:
		1 * log.log( { status ->
			status != null &&
			status.severity == IStatus.INFO &&
			status.plugin == 'bundle.symbolic.name' &&
			status.code == IStatus.OK &&
			status.message == 'informational message' &&
			status.exception == null
		} )
		0 * log._
	}
	
	def "The Logger always writes warning messages"() {
		
		given:
		ILog log = Mock(ILog)
		
		and:
		LogProvidingPlugin plugin = Mock(LogProvidingPlugin) {
			getBundle() >> Mock(Bundle) {
				getSymbolicName() >> 'bundle.symbolic.name'
				0 * _
			}
			getLog() >> log
			0 * _
		}
		
		and:
		Logger logger = Logger.getLogger(plugin)
		
		when:
		logger.warning('warning message')
		
		then:
		1 * log.log( { status ->
			status != null &&
			status.severity == IStatus.WARNING &&
			status.plugin == 'bundle.symbolic.name' &&
			status.code == IStatus.OK &&
			status.message == 'warning message' &&
			status.exception == null
		} )
		0 * log._
	}
	
	def "The Logger always writes error messages"() {

		given:
		ILog log = Mock(ILog)

		and:
		LogProvidingPlugin plugin = Mock(LogProvidingPlugin) {
			getBundle() >> Mock(Bundle) {
				getSymbolicName() >> 'bundle.symbolic.name'
				0 * _
			}
			getLog() >> log
			0 * _
		}
		
		and:
		Throwable mockThrowable = Mock(Throwable)
		
		and:
		Logger logger = Logger.getLogger(plugin)
		
		when:
		logger.error('error message', mockThrowable)
		
		then:
		1 * log.log( { status ->
			status != null &&
			status.severity == IStatus.ERROR &&
			status.plugin == 'bundle.symbolic.name' &&
			status.code == IStatus.OK &&
			status.message == 'error message' &&
			status.exception.is(mockThrowable)
		} )
		0 * log._
	}
	
	def "The Logger writes debug messages (only) when enabled in the log providing plugin"() {
		
		given:
		ILog log = Mock(ILog)
		
		and:
		LogProvidingPlugin plugin = Mock(LogProvidingPlugin) {
			getBundle() >> Mock(Bundle) {
				getSymbolicName() >> 'bundle.symbolic.name'
				0 * _
			}
			getLog() >> log
			isDebugEnabled() >> true
			0 * _
		}
		
		and:
		Logger logger = Logger.getLogger(plugin)
		
		when:
		logger.debug('debug message')
		
		then:
		1 * log.log( { status ->
			status != null &&
			status.severity == IStatus.INFO &&
			status.plugin == 'bundle.symbolic.name' &&
			status.code == IStatus.OK &&
			status.message == 'debug message' &&
			status.exception == null
		} )
		0 * log._
	}
	
	def "The Logger does NOT write debug messages when disabled in the log providing plugin"() {
		
		given:
		ILog log = Mock(ILog)
		
		and:
		LogProvidingPlugin plugin = Mock(LogProvidingPlugin) {
			getBundle() >> Mock(Bundle) {
				getSymbolicName() >> 'bundle.symbolic.name'
				0 * _
			}
			getLog() >> log
			isDebugEnabled() >> false
			0 * _
		}
		
		and:
		Logger logger = Logger.getLogger(plugin)
		
		when:
		logger.debug('debug message')
		
		then:
		0 * log._
	}
	
	def "The Logger writes debug items (only) when enabled in the log providing plugin: CALLING_METHOD"() {
		
		given:
		ILog log = Mock(ILog)
		
		and:
		LogProvidingPlugin plugin = Mock(LogProvidingPlugin) {
			getBundle() >> Mock(Bundle) {
				getSymbolicName() >> 'bundle.symbolic.name'
				0 * _
			}
			getLog() >> log
			isDebugEnabled() >> true
			0 * _
		}
		
		and:
		Logger logger = Logger.getLogger(plugin)
		
		when:
		logger.debug(DebugItem.CALLING_METHOD)
		
		then:
		1 * log.log( { status ->
			status != null &&
			status.severity == IStatus.INFO &&
			status.plugin == 'bundle.symbolic.name' &&
			status.code == IStatus.OK &&
			status.message.startsWith("Method ") && 
			status.message.contains(" was called from ")
			status.message.endsWith(")") &&
			status.exception == null
		} )		
		0 * log._
	}
	
	@Unroll("The Logger does NOT write debug items when they are null or not implemented or disabled disabled in the log providing plugin: #debugItem")
	def "The Logger does NOT write debug items when they are null or not implemented or disabled in the log providing plugin"() {
		
		given:
		ILog log = Mock(ILog)
		
		and:
		LogProvidingPlugin plugin = Mock(LogProvidingPlugin) {
			getBundle() >> Mock(Bundle) {
				getSymbolicName() >> 'bundle.symbolic.name'
				0 * _
			}
			getLog() >> log
			isDebugEnabled() >> debugEnabled
			0 * _
		}
		
		and:
		Logger logger = Logger.getLogger(plugin)
		
		when:
		logger.debug(debugItem as DebugItem)
		
		then:
		0 * log._
		
		where:
		debugEnabled | debugItem
		false 		 | null
		true 		 | null
		false 		 | DebugItem.CALLING_METHOD		
	}
	
	def "The debug message for DebugItem.CALLING_METHOD is only computed when 3 or more stack trace elements are available"() {
	
		given:
		Logger logger = Logger.getLogger(null)
		
		and:
		def stackTraceElements = [ 
			Mock(Logger.WrappedStackTraceElement) {
				0 * _
			},
			Mock(Logger.WrappedStackTraceElement) {
				0 * _
			},
			Mock(Logger.WrappedStackTraceElement) {
				0 * _
			}]
		
		when:
		String message = logger.createDebugMessageForCallingMethod('thread1', stackTraceElements)
		
		then:
		message == "[stackTraceElements.length <= 3: " + stackTraceElements.size() + "]";
	}
	
	@Unroll("The debug message for DebugItem.CALLING_METHOD is computed from the current thread name and stack trace elements: #label")
	def "The debug message for DebugItem.CALLING_METHOD is computed from the current thread name and stack trace elements"() {
		
		given:
		Logger logger = Logger.getLogger(null)
		
		and:
		def stackTraceElements = [ 
			Mock(Logger.WrappedStackTraceElement) {
				0 * _
			}, Mock(Logger.WrappedStackTraceElement) {
				0 * _
			}, Mock(Logger.WrappedStackTraceElement) {
				getClassName() >> 'ThirdClass'
				getMethodName() >> 'thirdMethod'
				0 * _
			},
			Mock(Logger.WrappedStackTraceElement) {
				getClassName() >> fourthClassFullName
				getSimpleClassName() >> 'FourthClass'
				getMethodName() >> 'fourthMethod'
				getLineNumber() >> 12345
				0 * _
			} ]
		
		when:
		String message = logger.createDebugMessageForCallingMethod('thread1', stackTraceElements)
		
		then:
		message == "Method thirdMethod(...) in class ThirdClass\n       was called from ${fourthClassFullName}.fourthMethod(FourthClass.java:12345)\n       (thread=thread1)";
		
		where:
		label 				| fourthClassFullName
		'simple class name' | 'FourthClass'
		'full class name' 	| 'x.y.z.FourthClass'
	}
	
}
