package data.converter.binding

import grails.databinding.converters.ValueConverter
import groovy.transform.CompileStatic
import org.springframework.web.multipart.MultipartFile

@CompileStatic
class AttachmentConverter implements ValueConverter {

    @Override
    boolean canConvert(Object value) {
        return value instanceof MultipartFile
    }

    @Override
    Object convert(Object value) {
        def mpf = value as MultipartFile
        return new Attachment(filename: mpf.originalFilename, bytes: mpf.bytes)
    }

    @Override
    Class<?> getTargetType() {
        return Attachment
    }
}
