package data.converter.binding

class Attachment {

    String filename
    byte[] bytes

    static constraints = {

    }

    static mapping = {
        bytes type: 'materialized_blob'
    }
}
