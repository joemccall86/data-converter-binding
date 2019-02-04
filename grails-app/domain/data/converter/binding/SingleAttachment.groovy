package data.converter.binding

import grails.rest.Resource

@Resource(uri = '/single-attachments')
class SingleAttachment {

    String text

    Attachment attachment

    static constraints = {
    }

    static mapping = {
        text type: 'text'
    }
}
