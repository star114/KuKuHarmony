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
    definition (name: "KuKu Harmony_Aircon", namespace: "turlvo", author: "KuKu") {
        capability "Actuator"
        capability "Switch"
        capability "Refresh"
        capability "Sensor"
        capability "Configuration"
        capability "Health Check"

        command "tempup"
        command "mode"
        command "jetcool"
        command "tempdown"
        command "speed"
        command "setRangedLevel", ["number"]

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

        standardTile ("tempup", "device.tempup", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "yes", label: "TEMP UP", action: "tempup"
            state "no", label: "unavail", action: ""
        }
        standardTile ("mode", "device.mode", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "yes", label: "MODE", action: "mode"
            state "no", label: "unavail", action: ""
        }

        standardTile ("jetcool", "device.jetcool", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "yes", label: "JET MODE", action: "jetcool"
            state "no", label: "unavail", action: ""
        }
        standardTile ("tempdown", "device.tempdown", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "yes", label: "TEMP DOWN", action: "tempdown"
            state "no", label: "unavail", action: ""
        }
        standardTile ("speed", "device.speed", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "yes", label: "FAN SPEED", action: "speed"
            state "no", label: "unavail", action: ""
        }
        controlTile ("tempSliderControl", "device.level", "slider", range:"(18..30)", height: 2, width: 4) {
            state "level", action:"setRangedLevel"
        }
        valueTile ("tempSliderControlValue", "device.level", height: 2, width: 2) {
            state "range", label:'Temperature\n${currentValue}°C', defaultState: true
        }

    }

    main(["switch"])
    details(["tempup", "mode",
            "jetcool", "tempdown", "speed",
            "tempSliderControl", "tempSliderControlValue"])
}

def installed() {
    log.debug "installed()"
    //configure()
}

// parse events into attributes
def parse(String description) {
    log.debug "Parsing '${description}'"
}

def tempup() {
    log.debug "child tempup()"
    parent.command(this, "tempup")
}

def mode() {
    log.debug "child mode()"
    parent.command(this, "mode")
}

def jetcool() {
    log.debug "child jetcool()"
    parent.command(this, "jetcool")
}

def tempdown() {
    log.debug "child tempdown()"
    parent.command(this, "tempdown")
}

def speed() {
    log.debug "child speed()"
    parent.command(this, "speed")

}

def setRangedLevel(value) {
    log.debug "setting ranged level to $value"
    parent.commandValue(this, value)
    sendEvent(name: "switch", value: "on")
    sendEvent(name:"level", value:value)
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


def momentaryOnHandler() {
    log.debug "momentaryOnHandler()"
    sendEvent(name: "switch", value: "off")
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

def poll() {
    log.debug "poll()"
}

def parseEventData(Map results) {
    results.each { name, value ->
        //Parse events and optionally create SmartThings events
    }
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
