package ui.configuration

import csstype.AlignSelf
import csstype.Display
import csstype.Length
import csstype.Margin
import csstype.Overflow
import csstype.TextOverflow
import csstype.VerticalAlign
import csstype.WhiteSpace
import csstype.em
import csstype.px
import emotion.react.css
import kotlinx.js.jso
import mui.icons.material.ArrowDownward
import mui.icons.material.ArrowUpward
import mui.icons.material.HelpOutline
import mui.icons.material.VerticalAlignTop
import mui.material.Button
import mui.material.ButtonColor
import mui.material.ButtonVariant
import mui.material.FormGroup
import mui.material.IconButton
import mui.material.IconButtonColor
import mui.material.Paper
import mui.material.Tooltip
import mui.material.TooltipPlacement
import mui.material.Typography
import mui.material.styles.TypographyVariant
import react.ChildrenBuilder
import react.ElementType
import react.ReactNode
import react.dom.html.ReactHTML.div
import ui.TrackOrderState
import ui.appTheme
import ui.common.SubProps
import ui.common.configurationSwitch
import ui.common.subFC
import ui.strings.Strings
import ui.strings.string

external interface TrackOrderProps : SubProps<TrackOrderState>

val TrackOrderBlock =
    subFC<TrackOrderProps, TrackOrderState> { _, state, editState ->
        FormGroup {
            div {
                configurationSwitch(
                    isOn = state.isOn,
                    onSwitched = { editState { copy(isOn = it) } },
                    labelStrings = Strings.TrackOrder,
                )
                Tooltip {
                    title = ReactNode(string(Strings.TrackOrderDescription))
                    placement = TooltipPlacement.right
                    disableInteractive = false
                    HelpOutline {
                        style =
                            jso {
                                verticalAlign = VerticalAlign.middle
                            }
                    }
                }
            }
        }
        if (state.isOn) buildTrackOrderDetail(state, editState)
    }

private fun ChildrenBuilder.buildTrackOrderDetail(
    state: TrackOrderState,
    editState: (TrackOrderState.() -> TrackOrderState) -> Unit,
) {
    div {
        css {
            margin = Margin(horizontal = 40.px, vertical = 0.px)
            width = Length.maxContent
        }
        Paper {
            elevation = 0
            div {
                css {
                    margin =
                        Margin(
                            horizontal = 24.px,
                            top = 16.px,
                            bottom = 24.px,
                        )
                    paddingTop = 8.px
                    paddingBottom = 8.px
                }
                state.order.indices.forEach { index ->
                    buildTrackOrderItem(index, state, editState)
                }
                div {
                    Button {
                        color = ButtonColor.secondary
                        variant = ButtonVariant.text
                        VerticalAlignTop()
                        onClick = { editState { moveMainTracksToTop() } }
                        div {
                            css { padding = 8.px }
                            +string(Strings.TrackOrderMoveMainToTopButton)
                        }
                    }
                }
            }
        }
    }
}

private fun ChildrenBuilder.buildTrackOrderItem(
    index: Int,
    state: TrackOrderState,
    editState: (TrackOrderState.() -> TrackOrderState) -> Unit,
) {
    div {
        css {
            display = Display.flex
            marginBottom = 16.px
        }
        Typography {
            css {
                color = appTheme.palette.secondary.main
                alignSelf = AlignSelf.center
            }
            variant = TypographyVariant.subtitle2
            component = "span".asDynamic().unsafeCast<ElementType<*>>()
            +string(Strings.TrackOrderItemLabel, "number" to (index + 1).toString())
        }
        Typography {
            style =
                jso {
                    marginLeft = 2.em
                    width = 8.em
                    whiteSpace = WhiteSpace.nowrap
                    overflow = Overflow.hidden
                    textOverflow = TextOverflow.ellipsis
                    alignSelf = AlignSelf.center
                }
            variant = TypographyVariant.body2
            component = "span".asDynamic().unsafeCast<ElementType<*>>()
            +state.trackNames[state.order[index]]
        }
        IconButton {
            color = IconButtonColor.inherit
            disabled = index == 0
            style =
                jso {
                    margin = 5.px
                    marginLeft = 20.px
                    height = Length.fitContent
                    alignSelf = AlignSelf.center
                }
            onClick = {
                editState { moveUp(index) }
            }
            ArrowUpward()
        }
        IconButton {
            color = IconButtonColor.inherit
            disabled = index == state.order.size - 1
            style =
                jso {
                    margin = 5.px
                    height = Length.fitContent
                    alignSelf = AlignSelf.center
                }
            onClick = {
                editState { moveDown(index) }
            }
            ArrowDownward()
        }
    }
}
