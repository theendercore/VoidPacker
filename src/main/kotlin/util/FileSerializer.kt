package com.theendercore.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.io.File

class FileSerializer : KSerializer<File> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("file", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: File) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): File = File(decoder.decodeString())
}
