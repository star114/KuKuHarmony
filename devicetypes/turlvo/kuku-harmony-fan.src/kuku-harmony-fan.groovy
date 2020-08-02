/**
 *  KuKu Harmony - Virtual Switch for Logitech Harmony
 *
 *  Copyright 2017 KuKu <turlvo@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

metadata {
    definition (name: "KuKu Harmony_Fan", namespace: "turlvo", author: "KuKu") {
        capability "Actuator"
        capability "Switch"
        capability "Refresh"
        capability "Sensor"
        capability "Configuration"
        capability "Health Check"

        command "speed"
        command "swing"
        command "timer"

        command "custom1"
        command "custom2"
        command "custom3"
        command "custom4"
        command "custom5"

        command "virtualOn"
        command "virtualOff"
    }

    tiles (scale: 2){
        standardTile ("actionFlat", "device.switch", width: 2, height: 2, decoration: "flat") {
            state "off", label: '${currentValue}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState:"turningOn"
            state "on", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"turningOff"
            state "off", label: '${currentValue}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState:"turningOn"
            state "on", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00a0dc", nextState:"turningOff"
        }

        standardTile ("speed", "device.speed", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "speed", label: "SPEED", action: "speed", defaultState: true
        }
        standardTile ("swing", "device.swing", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "swing", label: "SWING", action: "swing", defaultState: true
        }

        standardTile ("timer", "device.timer", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "timer", label: "TIMER", action: "timer", defaultState: true
        }

        standardTile ("custom1", "device.custom1", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "custom1", label: "custom1", action: "custom1", defaultState: true
        }
        standardTile ("custom2", "device.custom2", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "custom2", label: "custom2", action: "custom2", defaultState: true
        }
        standardTile ("custom3", "device.custom3", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "custom3", label: "custom3", action: "custom3", defaultState: true
        }
        standardTile ("custom4", "device.custom4", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "custom4", label: "custom4", action: "custom4", defaultState: true
        }
        standardTile ("custom5", "device.custom5", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "custom5", label: "custom5", action: "custom5", defaultState: true
        }
    }

    main(["switch"])
    details(["switch", "speed", "swing", "timer",
            "custom1", "custom2", "custom3", "custom4", "custom5"])
}

def installed() {
    log.debug "installed()"
    //configure()
}

// parse events into attributes
def parse(String description) {
    log.debug "Parsing '${description}'"
}

def speed() {
    log.debug "child speed()"
    parent.command(this, "speed")
}

def swing() {
    log.debug "child swing()"
    parent.command(this, "swing")
}

def timer() {
    log.debug "child timer()"
    parent.command(this, "timer")
}

def custom1() {
    log.debug "child custom1()"
    parent.command(this, "custom1")
}

def custom2() {
    log.debug "child custom2()"
    parent.command(this, "custom2")
}

def custom3() {
    log.debug "child custom3()"
    parent.command(this, "custom3")
}

def custom4() {
    log.debug "child custom4()"
    parent.command(this, "custom4")
}

def custom5() {
    log.debug "child custom5()"
    parent.command(this, "custom5")
}


def on() {
    log.debug "child on()"

    log.debug "on>> ${device.currentState("switch")?.value}"
    def currentState = device.currentState("switch")?.value

    if (currentState == "on") {
        log.debug "Already turned on, skip ON command"
    } else {
        parent.command(this, "power-on")
        sendEvent(name: "switch", value: "on")
    }
}

def off() {
    log.debug "child off"

    log.debug "off>> ${device.currentState("switch")?.value}"
    def currentState = device.currentState("switch")?.value

    if (currentState == "on") {
        parent.command(this, "power-off")
        sendEvent(name: "switch", value: "off")

    } else {
        log.debug "Already turned off, skip OFF command"
    }
}

def virtualOn() {
    log.debug "child on()"
    sendEvent(name: "switch", value: "on")
}

def virtualOff() {
    log.debug "child off"
    sendEvent(name: "switch", value: "off")
}

def generateEvent(Map results) {
    results.each { name, value ->
        log.debug "generateEvent>> name: $name, value: $value"
        def currentState = device.currentValue("switch")
        log.debug "generateEvent>> currentState: $currentState"
        if (currentState != value) {
            log.debug "generateEvent>> changed to $value"
            sendEvent(name: "switch", value: value)
        } else {
            log.debug "generateEvent>> not change"
        }
    }
    return null
}

def poll() {
    log.debug "poll()"
}

def parseEventData(Map results) {
    results.each { name, value ->
        //Parse events and optionally create SmartThings events
    }
}
