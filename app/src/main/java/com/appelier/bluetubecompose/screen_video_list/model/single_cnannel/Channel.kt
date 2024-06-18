package com.appelier.bluetubecompose.screen_video_list.model.single_cnannel

import com.appelier.bluetubecompose.screen_video_list.model.ThumbnailAttributes
import com.appelier.bluetubecompose.screen_video_list.model.Thumbnails
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Channel(
    val id: String = "",
    val snippet: ChannelContent = ChannelContent()
) {

    companion object {

        val DEFAULT_CHANNEL_1 = Channel(
            id = "UC33-OkQ6VCYk8xtml8Pk4-g",
            snippet = ChannelContent(
                title = "NRL - National Rugby League",
                description = "The Official YouTube Channel of the National Rugby League.  \n\nEvery week will feature exclusive interviews, highlights, and the greatest moments that the NRL has ever produced!\n\nSubscribe and hit that notification bell to know when new videos are available here.\n\nFollow us on social @nrl and visit NRL.com for all the latest news and updates!",
                publishedAt = "2006-05-08T04:46:04Z",
                thumbnails = Thumbnails(
                    medium = ThumbnailAttributes(
                        url = "https://yt3.ggpht.com/Pui0xFjDp_F-x0UK5Rkfh1tGkwZLy7fkU4Pr44Zht3DJcMwWXLnxoVIGSRS5Pr7SVxgES0-ouA=s240-c-k-c0x00ffffff-no-rj",
                        height = 240,
                        width = 240
                    )
                )
            )
        )

        val DEFAULT_CHANNEL_2 = Channel(
            id = "UChBEbMKI1eCcejTtmI32UEw",
            snippet = ChannelContent(
                title = "Joshua Weissman",
                description = "Hi, I'm Josh and I make entertaining, exciting, and inspirational videos about food and cooking. I'm obsessive about bread, and I like to eat things that have a large quantity of butter in them. All I want is to entertain and excite YOU about food, and maybe inspire you to go cook something yourself. \n\nFind me on: \nhttp://instagram.com/joshuaweissman\nhttp://twitter.com/therealweissman\nhttp://facebook.com/thejoshuaweissman\nhttp://joshuaweissman.com\n\nFor any Business, brand or partnership enquiries Email: josh.cohen@oddprojects.com",
                publishedAt = "2014-02-28T00:03:34Z",
                thumbnails = Thumbnails(
                    medium = ThumbnailAttributes(
                        url = "https://yt3.ggpht.com/ytc/AIdro_nfXRvoxu5cFt2H4WhJfFLbL5SVdzmvEnFymnPzH3_1qPM=s240-c-k-c0x00ffffff-no-rj",
                        height = 240,
                        width = 240
                    )
                )
            )
        )

        val DEFAULT_CHANNEL_3 = Channel(
            id = "UC6huXz0F6-7KA7-mW0jdejA",
            snippet = ChannelContent(
                title = "ErikTheElectric",
                description = "I'm Erik, I like food.",
                publishedAt = "2013-08-10T22:51:45Z",
                thumbnails = Thumbnails(
                    medium = ThumbnailAttributes(
                        url = "https://yt3.ggpht.com/ytc/AIdro_nDTKq9EfTx0SA4Tv4FV9MfCh7zLXl0p4m2CjBwVyVybs4=s240-c-k-c0x00ffffff-no-rj",
                        height = 240,
                        width = 240
                    )
                )
            )
        )

        val DEFAULT_CHANNEL_4 = Channel(
            id = "UCLDAM_6WwevNIPHz7IalMLw",
            snippet = ChannelContent(
                title = "Noriel",
                description = "Danger",
                publishedAt = "2011-12-20T21:43:22Z",
                thumbnails = Thumbnails(
                    medium = ThumbnailAttributes(
                        url = "https://yt3.ggpht.com/MamDoeUwqyGIowFfBJ4teXOuHSuwpUdSwv_EcanSB99-VIKxExaoEpm80qwEo5uiaGJCYXuGiw=s240-c-k-c0x00ffffff-no-nd-rj",
                        height = 240,
                        width = 240
                    )
                )
            )
        )

        val DEFAULT_CHANNEL_5 = Channel(
            id = "UCS5Oz6CHmeoF7vSad0qqXfw",
            snippet = ChannelContent(
                title ="DanTDM",
                description = "non-daily videos..\n\nBUSINESS ONLY :: management@dantdmteam.com",
                publishedAt = "2012-07-14T21:58:18Z",
                thumbnails = Thumbnails(
                    medium = ThumbnailAttributes(
                        url = "https://yt3.ggpht.com/_SQKtbdn1Xv6vkV_OX5D1jTNQY2SAQ5Gq-WSPlTbc8h5qMHQ3dnwpSXlpq8ADikltH3zC9KC5A=s240-c-k-c0x00ffffff-no-rj",
                        height = 240,
                        width = 240
                    )
                )
            )
        )
    }
}
