package data.converter.binding

import grails.rest.Resource

@Resource(uri = "/comments")
class Comment {

    String text

    static  hasMany = [attachments: Attachment]

    static constraints = {
    }

    static mapping = {
        text type: 'text'
    }
}
