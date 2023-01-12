package cc.trixey.invero.bukkit.panel

import cc.trixey.invero.bukkit.element.item.BaseItem
import cc.trixey.invero.common.Pos
import cc.trixey.invero.common.Scale
import cc.trixey.invero.common.panel.GeneratorPanel
import cc.trixey.invero.common.panel.PanelContainer
import cc.trixey.invero.common.panel.PanelWeight

/**
 * Invero
 * cc.trixey.invero.bukkit.panel.ScrollGeneratorPanel
 *
 * @author Arasple
 * @since 2023/1/11 17:16
 */
class ScrollGeneratorPanel<T>(
    parent: PanelContainer,
    weight: PanelWeight,
    scale: Scale,
    locate: Pos
) : ScrollStandardPanel(parent, weight, scale, locate), GeneratorPanel<T, BaseItem<*>> {

    override var sourceElements: List<T> = listOf()

    override val outputElements by lazy {
        ArrayList(arrayOfNulls<BaseItem<*>?>(sourceElements.size).toList())
    }

    override var generator: (T) -> BaseItem<*>? = { null }

    override val generatorPool: List<Pos> by lazy {
        (scale.getArea() - elements.occupiedPositions()).sorted()
    }

    private var initialized: Boolean = false

    private fun initialize() {
        if (initialized) return

        sourceElements
            .windowed(columCapacity, columCapacity, true)
            .forEach { columElements ->

                insertColum {
                    if (it in columElements.indices) {
                        val element = columElements[it]
                        val index = sourceElements.indexOf(element)

                        getOutput(index)
                    } else null
                }
            }

        initialized = true
    }


    override fun render() {
        initialize()
        super<ScrollStandardPanel>.render()
    }

}