package ru.chumak.mvikotlincounter.instancekeeper

import com.arkivanov.essenty.instancekeeper.InstanceKeeper

/**
 *
 *
 * @author Evgeny Chumak
 **/
class DesktopInstanceKeeper : InstanceKeeper {
    private val instances = mutableMapOf<Any, InstanceKeeper.Instance>()

    override fun get(key: Any): InstanceKeeper.Instance? =
        instances[key]

    override fun put(key: Any, instance: InstanceKeeper.Instance) {
        instances[key] = instance
    }

    override fun remove(key: Any): InstanceKeeper.Instance? {
        return instances.remove(key)
    }
}