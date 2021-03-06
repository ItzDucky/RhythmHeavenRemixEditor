package io.github.chrislo27.rhre3.editor.rendering

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Align
import io.github.chrislo27.rhre3.editor.Editor
import io.github.chrislo27.rhre3.editor.Tool
import io.github.chrislo27.rhre3.track.PlayState
import io.github.chrislo27.rhre3.util.scaleFont
import io.github.chrislo27.rhre3.util.unscaleFont
import io.github.chrislo27.toolboks.registry.AssetRegistry
import io.github.chrislo27.toolboks.util.MathHelper
import io.github.chrislo27.toolboks.util.gdxutils.*
import kotlin.math.absoluteValue


fun Editor.renderPlayYan(batch: SpriteBatch) {
    val beat = if (remix.playState != PlayState.STOPPED) remix.beat else remix.playbackStart
    fun drawWalking() {
        val step = (MathHelper.getSawtoothWave(0.25f) * 4).toInt()
        batch.draw(AssetRegistry.get<Texture>("playyan_walking"), beat,
                   remix.trackCount * 1f,
                   toScaleX(26f), toScaleY(35f),
                   step * 26, 0, 26, 35, false, false)
    }
    if (remix.playState != PlayState.STOPPED) {
        val beatPercent = beat % 1f
        val playbackStartPercent = remix.playbackStart % 1f
        val floorPbStart = Math.floor(playbackStartPercent.toDouble()).toFloat()
        val currentSwing = remix.tempos.swingAt(beat)
        val jumpHeight: Float = MathUtils.sin(MathUtils.PI * (if (playbackStartPercent > 0f && remix.beat < floorPbStart + 1f) (beat - remix.playbackStart) / (1f - remix.playbackStart % 1f) else beatPercent)).absoluteValue

        batch.draw(AssetRegistry.get<Texture>(if (currentSwing.ratio == 50) "playyan_jumping" else "playyan_pogo"), beat,
                   remix.trackCount + 1f * jumpHeight, toScaleX(26f), toScaleY(35f),
                   0, 0, 26, 35, false, false)
    } else {
        drawWalking()
    }
}

fun Editor.renderTimeSignatures(batch: SpriteBatch) {
    val timeSignatures = remix.timeSignatures
    val bigFont = main.timeSignatureFont
    val heightOfTrack = remix.trackCount.toFloat() - toScaleY(Editor.TRACK_LINE_THICKNESS) * 2f
    val inputBeat = Math.floor(camera.getInputX().toDouble()).toInt()
    bigFont.scaleFont(camera)
    bigFont.scaleMul((heightOfTrack * 0.5f - 0.075f * (heightOfTrack / Editor.DEFAULT_TRACK_COUNT)) / bigFont.capHeight)

    fun renderTimeSignature(beat: Int, lowerText: String, upperText: String) {
        val x = beat
        val startY = 0f + toScaleY(Editor.TRACK_LINE_THICKNESS)
        val maxWidth = 1f

        val lowerWidth = bigFont.getTextWidth(lowerText, 1f, false).coerceAtMost(maxWidth)
        val upperWidth = bigFont.getTextWidth(upperText, 1f, false).coerceAtMost(maxWidth)
        val biggerWidth = Math.max(lowerWidth, upperWidth)

        bigFont.drawCompressed(batch, lowerText,
                               x + biggerWidth * 0.5f - lowerWidth * 0.5f,
                               startY + bigFont.capHeight,
                               maxWidth, Align.left)
        bigFont.drawCompressed(batch, upperText,
                               x + biggerWidth * 0.5f - upperWidth * 0.5f,
                               startY + heightOfTrack,
                               maxWidth, Align.left)
    }

    timeSignatures.map.values.forEach { timeSig ->
        if (currentTool == Tool.TIME_SIGNATURE && timeSig.beat == inputBeat) {
            bigFont.color = theme.selection.selectionBorder
        } else {
            bigFont.setColor(theme.trackLine.r, theme.trackLine.g, theme.trackLine.b, theme.trackLine.a * 0.75f)
        }

        renderTimeSignature(timeSig.beat, timeSig.lowerText, timeSig.upperText)
    }

    if (currentTool == Tool.TIME_SIGNATURE && remix.timeSignatures.map[inputBeat] == null && remix.playState == PlayState.STOPPED) {
        bigFont.setColor(theme.trackLine.r, theme.trackLine.g, theme.trackLine.b, theme.trackLine.a * MathUtils.lerp(0.2f, 0.35f, MathHelper.getTriangleWave(2f)))
        val last = remix.timeSignatures.getTimeSignature(inputBeat.toFloat())
        renderTimeSignature(inputBeat, last?.lowerText ?: "4", last?.upperText ?: "4")
    }

    bigFont.setColor(1f, 1f, 1f, 1f)
    bigFont.unscaleFont()
}

fun Editor.renderBeatNumbers(batch: SpriteBatch, beatRange: IntRange, font: BitmapFont) {
    for (i in beatRange) {
        val width = Editor.ENTITY_WIDTH * 0.4f
        val x = i - width / 2f
        val y = remix.trackCount + toScaleY(Editor.TRACK_LINE_THICKNESS + Editor.TRACK_LINE_THICKNESS) + font.capHeight
        val text = if (i == 0) Editor.ZERO_BEAT_SYMBOL else "${Math.abs(i)}"
        if (stage.jumpToField.hasFocus && i == stage.jumpToField.text.toIntOrNull() ?: Int.MAX_VALUE) {
            val glow = MathHelper.getTriangleWave(1f)
            val sel = theme.selection.selectionBorder
            font.setColor(MathUtils.lerp(sel.r, 1f, glow), MathUtils.lerp(sel.g, 1f, glow),
                          MathUtils.lerp(sel.b, 1f, glow), sel.a)
        } else {
            font.color = theme.trackLine
        }
        font.drawCompressed(batch, text,
                            x, y, width, Align.center)
        if (i < 0) {
            val textWidth = font.getTextWidth(text, width, false)
            font.drawCompressed(batch, Editor.NEGATIVE_SYMBOL, x - textWidth / 2f, y, Editor.ENTITY_WIDTH * 0.2f, Align.right)
        }

        val measureNum = remix.timeSignatures.getMeasure(i.toFloat())
        if (measureNum >= 1 && remix.timeSignatures.getMeasurePart(i.toFloat()) == 0 && i < remix.duration) {
            font.setColor(theme.trackLine.r, theme.trackLine.g, theme.trackLine.b, theme.trackLine.a * 0.5f)
            font.drawCompressed(batch, "$measureNum",
                                x, y + font.lineHeight, width, Align.center)
        }
    }
    font.setColor(1f, 1f, 1f, 1f)
}

fun Editor.renderBeatLines(batch: SpriteBatch, beatRange: IntRange, trackYOffset: Float, updateDelta: Boolean) {
    for (i in beatRange) {
        batch.color = theme.trackLine
        if (remix.timeSignatures.getMeasurePart(i.toFloat()) > 0) {
            batch.setColor(theme.trackLine.r, theme.trackLine.g, theme.trackLine.b, theme.trackLine.a * 0.25f)
        }

        val xOffset = toScaleX(Editor.TRACK_LINE_THICKNESS) / -2
        batch.fillRect(i.toFloat() + xOffset, trackYOffset, toScaleX(Editor.TRACK_LINE_THICKNESS),
                       remix.trackCount + toScaleY(Editor.TRACK_LINE_THICKNESS))

        val flashAnimation = subbeatSection.flashAnimation > 0
        val actuallyInRange = (subbeatSection.enabled && i.toFloat() in subbeatSection.start..subbeatSection.end)
        if (flashAnimation || actuallyInRange) {
            batch.setColor(theme.trackLine.r, theme.trackLine.g, theme.trackLine.b,
                           theme.trackLine.a * 0.3f *
                                   if (!actuallyInRange) subbeatSection.flashAnimation else 1f)
            for (j in 1 until Math.round(1f / snap)) {
                batch.fillRect(i.toFloat() + snap * j + xOffset, trackYOffset, toScaleX(Editor.TRACK_LINE_THICKNESS),
                               remix.trackCount + toScaleY(Editor.TRACK_LINE_THICKNESS))
            }
        }
    }
    batch.setColor(1f, 1f, 1f, 1f)

    if (subbeatSection.flashAnimation > 0 && updateDelta) {
        subbeatSection.flashAnimation -= Gdx.graphics.deltaTime / subbeatSection.flashAnimationSpeed
        if (subbeatSection.flashAnimation < 0)
            subbeatSection.flashAnimation = 0f
    }
}

fun Editor.renderHorizontalTrackLines(batch: SpriteBatch, startX: Float, width: Float, trackYOffset: Float) {
    batch.color = theme.trackLine
    for (i in 0..remix.trackCount) {
        batch.fillRect(startX, trackYOffset + i.toFloat(), width,
                       toScaleY(Editor.TRACK_LINE_THICKNESS))
    }
    batch.setColor(1f, 1f, 1f, 1f)
}

fun Editor.renderImplicitTempo(batch: SpriteBatch) {
    val text = "The tempo is implicitly set to ${remix.tempos.defaultTempo} BPM. Please set the tempo explicitly using the Tempo Change tool.   "
    val f = main.defaultBorderedFontLarge
    f.setColor(1f, 1f, 1f, 1f)
    f.scaleMul(0.5f)

    for (i in 0 until 12) {
        val width = f.getTextWidth(text)
        val sign = if (i % 2 == 0) -1 else 1
        val scroll = MathHelper.getSawtoothWave(System.currentTimeMillis() + i * 1234L, 12.5f + sign * i * 0.75f)
        f.color = Color().setHSB(scroll, 1f, 1f)
        f.draw(batch, text, 0f + sign * width * scroll, i * main.defaultCamera.viewportHeight / 8f)
        f.draw(batch, text, -sign * width + sign * width * scroll, i * main.defaultCamera.viewportHeight / 8f)
    }
    f.setColor(1f, 1f, 1f, 1f)
    f.scaleMul(1 / 0.5f)
}
