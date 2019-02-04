package data.converter.binding

import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.multipart.MultipartBody
import org.grails.io.support.ClassPathResource
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Integration
@Rollback
class CommentSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient httpClient

    def setup() {
        httpClient = HttpClient.create(new URL("http://localhost:$serverPort"))
    }

    // This does not
    def 'can create comment with attachment'() {
        when:

        def response = httpClient.toBlocking().exchange(
            HttpRequest.POST("/comments", MultipartBody.builder()
                .addPart(
                    'text',
                    'This is some text for a comment with an attachment')
                .addPart(
                    'attachments[0]',
                    'test.jpg',
                    MediaType.APPLICATION_OCTET_STREAM_TYPE,
                    new ClassPathResource('test.jpg').file)
                .addPart(
                    'attachments[1]',
                    'test2.jpg',
                    MediaType.APPLICATION_OCTET_STREAM_TYPE,
                    new ClassPathResource('test.jpg').file)
                .build())
                    .contentType(MediaType.MULTIPART_FORM_DATA_TYPE),
            Comment
        )

        then:
        response.status == HttpStatus.CREATED
        response.body().id != null

        and: 'the actual GORM objects exist as well'
        Comment.count == 1 + old(Comment.count)
        Attachment.count == 1 + old(Attachment.count)
    }

    // This passes
    def 'can create standalone attachments'() {
        when:
        def response = httpClient.toBlocking().exchange(
            HttpRequest.POST("/single-attachments", MultipartBody.builder()
                .addPart(
                    'text',
                    'This is some text for a single attachment')
                .addPart(
                    'attachment',
                    'test.jpg',
                    MediaType.APPLICATION_OCTET_STREAM_TYPE,
                    new ClassPathResource('test.jpg').file)
                .build())
                .contentType(MediaType.MULTIPART_FORM_DATA_TYPE),
            SingleAttachment
        )

        then:
        response.status == HttpStatus.CREATED
        response.body().id != null

        and: 'the actual GORM objects exist as well'
        SingleAttachment.count == 1 + old(SingleAttachment.count)
        Attachment.count == 1 + old(Attachment.count)
    }
}
