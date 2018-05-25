@file:Suppress("UNCHECKED_CAST")

package registry

import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.discovery.ASMDataTable
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.HashMap
import java.util.HashSet

abstract class Registry<T : RegistryItem<*>, R, S> {
    var registry: Set<T> = HashSet()

    abstract val annotationName: String
    abstract val requiredSuperclass: Class<R>
    abstract val registryId: ResourceLocation

    fun populate(dataTable: ASMDataTable) {
        val anns = dataTable.getAll(annotationName)
        for (data in anns) {
            try {
                val name = data.className
                val clazz = Class.forName(name)
                for (a: Annotation in clazz.declaredAnnotations) {
                    val n = a.annotationClass.java.canonicalName
                    if (n == annotationName) {
                        if (requiredSuperclass == clazz.superclass) {
                            val cls = clazz.asSubclass(requiredSuperclass)
                            addItem(a as S, cls)
                        }
                    }
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }

        }
    }

    abstract fun addItem(annotation: S, annotatedClass: Class<out R>)
    abstract fun registerItems(items: Set<RegistryItem<*>>)
}

interface RegistryItem<out T>{
    val type: T
}

object RegistryManager {
    private val REGISTRIES = HashMap<ResourceLocation, Registry<out RegistryItem<*>, *, *>>()

    fun addRegistry(cls: Class<out Registry<out RegistryItem<*>, *, *>>) {
        try {
            val instance = cls.newInstance()
            REGISTRIES[instance.registryId] = instance
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

    fun startRegistries(dataTable: ASMDataTable) {
        for (reg in REGISTRIES.values) {
            reg.populate(dataTable)
            reg.registerItems(reg.registry)
        }
    }

    fun getRegistry(name: String): Registry<out RegistryItem<*>, *, *>? {
        val optional = REGISTRIES.values.stream().filter { it -> it.registryId.resourcePath == name }.findFirst()
        return if (optional.isPresent) {
            optional.get()
        } else null
    }
}

const val modid = "autoreglib"
const val name = "Automatic Registry Library"
const val version = "1.0"

@Mod(modid=modid, name=name, version=version, modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object RegistryLib{
    internal var asmDataTable: ASMDataTable? = null
    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent){
        asmDataTable = event.asmData
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent){
        RegistryManager.startRegistries(RegistryLib.asmDataTable!!)
    }
}
