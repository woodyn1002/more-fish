package me.elsiff.morefish.resource

import me.elsiff.morefish.protocollib.ProtocolLibHooker
import me.elsiff.morefish.resource.template.TemplateBundle
import org.bukkit.configuration.Configuration

/**
 * Created by elsiff on 2019-01-02.
 */
class ResourceBundle {
    lateinit var config: Configuration
    lateinit var fish: Configuration
    lateinit var lang: Configuration
    lateinit var templates: TemplateBundle
    var protocolLib = ProtocolLibHooker()
}