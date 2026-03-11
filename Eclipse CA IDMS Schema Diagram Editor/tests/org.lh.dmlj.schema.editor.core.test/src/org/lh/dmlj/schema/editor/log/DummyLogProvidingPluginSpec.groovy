/**
 * Copyright (C) 2018  Luc Hermans
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

import org.eclipse.core.runtime.ILogListener
import org.eclipse.core.runtime.IStatus

import spock.lang.Specification

class DummyLogProvidingPluginSpec extends Specification {
	
	def "The getBundle() method always throws an UnpportedOperationException"() {
		
		given:
		DummyLogProvidingPlugin dummy = new DummyLogProvidingPlugin()
		
		when:
		dummy.getBundle()
		
		then:
		thrown UnsupportedOperationException		
	}
	
	def "The isDebugEnabled() method always returns false"() {
		
		given:
		DummyLogProvidingPlugin dummy = new DummyLogProvidingPlugin()
		
		when:
		def debugEnabled = dummy.isDebugEnabled()
		
		then:
		!debugEnabled
	}
	
	def "The getLog() method returns an ILog object of which only the log(IStatus) method can be invoked"() {
		
		given:
		DummyLogProvidingPlugin dummy = new DummyLogProvidingPlugin()
		
		and:
		IStatus status = Mock(IStatus) {
			0 * _
		}
		
		
		when:
		def log = dummy.getLog()
		
		then:
		log
		
		when:
		log.log(status)
		
		then:
		notThrown UnsupportedOperationException		
		
		when:
		log.addLogListener(null as ILogListener)
		
		then:
		thrown UnsupportedOperationException
		
		when:
		log.getBundle()
		
		then:
		thrown UnsupportedOperationException
		
		when:
		log.removeLogListener(null as ILogListener)
		
		then:
		thrown UnsupportedOperationException
	}

}
