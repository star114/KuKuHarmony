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
        capability "Thermostat"
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

        command "virtualOn"
        command "virtualOff"
    }

    tiles(scale: 2) {
        multiAttributeTile(name:"thermostatBasic", type:"thermostat", width:6, height:4) {
            tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
                attributeState("temp", label:'${currentValue}', unit:"dC", defaultState: true,
                backgroundColors:[
                    [value: 18, color: "#153591"],
                    [value: 20, color: "#1e9cbb"],
                    [value: 22, color: "#90d2a7"],
                    [value: 24, color: "#44b621"],
                    [value: 26, color: "#f1d801"],
                    [value: 28, color: "#d04e00"],
                    [value: 30, color: "#bc2323"]
                ])
            }
            tileAttribute("device.temperature", key: "VALUE_CONTROL") {
                attributeState("VALUE_UP", action: "tempUp")
                attributeState("VALUE_DOWN", action: "tempDown")
            }
        }

        valueTile("temperature", "device.temperature", width: 2, height: 2) {
            state("temperature", label:'${currentValue}', unit:"dC",
                backgroundColors:[
                    [value: 18, color: "#153591"],
                    [value: 20, color: "#1e9cbb"],
                    [value: 22, color: "#90d2a7"],
                    [value: 24, color: "#44b621"],
                    [value: 26, color: "#f1d801"],
                    [value: 28, color: "#d04e00"],
                    [value: 30, color: "#bc2323"]
                ]
            )
        }

        standardTile("tempdown", "device.temperature", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
            state "tempdown", label:'down', action:"tempdown", defaultState: true
        }
        standardTile("tempup", "device.temperature", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
            state "tempup", label:'up', action:"tempup", defaultState: true
        }
        controlTile ("tempSliderControl", "device.level", "slider", range:"(18..30)", height: 2, width: 4) {
            state "level", action:"setRangedLevel"
        }
        valueTile ("tempSliderControlValue", "device.level", height: 2, width: 2) {
            state "range", label:'Temperature\n${currentValue}°C', defaultState: true
        }
        standardTile ("mode", "device.mode", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "mode", label: "MODE", action: "mode", defaultState: true
        }
        standardTile ("jetcool", "device.jetcool", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "jetcool", label: "JET MODE", action: "jetcool", defaultState: true
        }
        standardTile ("speed", "device.speed", width: 2, height: 2, decoration: "flat", canChangeIcon: false, canChangeBackground: false) {
            state "speed", label: "FAN SPEED", action: "speed", defaultState: true
        }
    }

    main("thermostatBasic")
    details([
        "thermostatBasic",
        "temperature", "tempdown", "tempup",
        "tempSliderControl", "tempSliderControlValue",
        "mode", "jetcool", "speed"
    ])
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
