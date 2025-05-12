package com.vlascitchii.data_local.enetity.video_list.videos

import androidx.room.Embedded
import androidx.room.Relation
import com.vlascitchii.data_local.enetity.PageEntity
import com.vlascitchii.data_local.enetity.video_list.ThumbnailAttributesEntity
import com.vlascitchii.data_local.enetity.video_list.ThumbnailsEntity
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

//TODO: check if the @Embedded annotation will insert the
// parentColumn = "currentPageToken" from the PageEntity
data class YoutubeVideoResponseEntity(
    @Embedded val pageEntity: PageEntity,
//    val nextPageToken: String? = null,
//    var currentPageToken: String = "",
//    val prevPageToken: String? = null,
    @Relation(
        parentColumn = "currentPageToken",
        entityColumn = "pageToken"
    )
    val items: List<YoutubeVideoEntity>
) {

    companion object {

        private val offsetFormatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val testDateTime: OffsetDateTime = offsetFormatter.parse("2007-12-03T10:15:30+01:00", OffsetDateTime::from)
        const val INITIAL_PAGE_TOKEN = ""

//TODO: compare with previous lists and delete this one if previous can be used for testing the database
        val TEST_DATABASE_VIDEO_LIST = listOf(
            YoutubeVideoEntity(
                id = "bbM6aSB6iMQ",
                INITIAL_PAGE_TOKEN,
                snippet = VideoSnippetEntity(
                    "State of Origin 2024 | Blues v Maroons | Match Highlights",
                    "The New South Wales Blues and the Queensland Maroons face off in Game 1 of the 2024 Ampol State of Origin series\n\n🏉 SUBSCRIBE FOR MORE NRL ACTION 🏉\nhttps://www.youtube.com/channel/UC33-OkQ6VCYk8xtml8Pk4-g?sub_confirmation=1\n\n🏉 Subscribe to the official NRLW Channel 🏉https://www.youtube.com/channel/UCME0EifJ3Xm3mGmLdBz1Kyw?sub_confirmation=1\n\nTo keep up to date with all the latest NRL content head to:\nNRL Watch: https://www.nrl.com/watch/\nNRL Tickets: https://tickets.nrl.com/\nNRL Draw: https://www.nrl.com/draw/",
                    "2024-06-05T12:22:55Z",
                    "NRL - National Rugby League",
                    "https://yt3.ggpht.com/Pui0xFjDp_F-x0UK5Rkfh1tGkwZLy7fkU4Pr44Zht3DJcMwWXLnxoVIGSRS5Pr7SVxgES0-ouA=s240-c-k-c0x00ffffff-no-rj",
                    channelId = "UC33-OkQ6VCYk8xtml8Pk4-g",
                    thumbnailsEntity = ThumbnailsEntity(
                        ThumbnailAttributesEntity(
                            "https://i.ytimg.com/vi/bbM6aSB6iMQ/mqdefault.jpg",
                            180,
                            320,
                            "bbM6aSB6iMQ",
                            "bbM6aSB6iMQ"
                        ),
                        "bbM6aSB6iMQ",
                        "bbM6aSB6iMQ"
                    ),
                    "bbM6aSB6iMQ",
                    "bbM6aSB6iMQ"
                ),
                statistics = VideoStatisticsEntity(
                    400242,
                    3593,
                    "bbM6aSB6iMQ",
                    "bbM6aSB6iMQ"
                ),
                contentDetailsEntity = ContentDetailsEntity(
                    "PT5M",
                    "bbM6aSB6iMQ",
                    "bbM6aSB6iMQ"
                ),
                testDateTime
            ),

            YoutubeVideoEntity(
                id = "990CmzkhEq4",
                INITIAL_PAGE_TOKEN,
                snippet = VideoSnippetEntity(
                    "I Tried The Best Taco In The World",
                    "It took a lot of tacos to get here.\n\nGet My Cookbook: https://bit.ly/TextureOverTaste\n\nAdditional Cookbook Options (other stores, international, etc.): https://bit.ly/WeissmanCookbook\n\nFOLLOW ME:\nInstagram: https://www.instagram.com/joshuaweissman\nTik Tok: https://www.tiktok.com/@flakeysalt\nTwitter: https://twitter.com/therealweissman\nFacebook: https://www.facebook.com/thejoshuaweissman\nSubreddit: https://www.reddit.com/r/JoshuaWeissman/\n---------------------------------------------------------------",
                    "2024-06-09T14:30:03Z",
                    "Joshua Weissman",
                    "https://yt3.ggpht.com/ytc/AIdro_nfXRvoxu5cFt2H4WhJfFLbL5SVdzmvEnFymnPzH3_1qPM=s240-c-k-c0x00ffffff-no-rj",
                    channelId = "UChBEbMKI1eCcejTtmI32UEw",
                    thumbnailsEntity = ThumbnailsEntity(
                        ThumbnailAttributesEntity(
                            "https://i.ytimg.com/vi/990CmzkhEq4/mqdefault.jpg",
                            180,
                            320,
                            "990CmzkhEq4",
                            "990CmzkhEq4"
                        ),
                        "990CmzkhEq4",
                        "990CmzkhEq4"
                    ),
                    "990CmzkhEq4",
                    "990CmzkhEq4"
                ),
                statistics = VideoStatisticsEntity(
                    698144,
                    25698,
                    "990CmzkhEq4",
                    "990CmzkhEq4"
                ),
                contentDetailsEntity = ContentDetailsEntity(
                    "PT30M38S",
                    "990CmzkhEq4",
                    "990CmzkhEq4"
                ),
                testDateTime
            ),

            YoutubeVideoEntity(
                id = "D7UqN8_kTpg",
                INITIAL_PAGE_TOKEN,
                snippet = VideoSnippetEntity(
                    "I Finished The ENTIRE Menu Everywhere My Tesla Took Me for a Week",
                    "Found out my tesla had a hungry button, this is what happened next.. \n\nSubscribe: https://www.youtube.com/c/ErikTheElectric?sub_confirmation=1 \n\n\nGet I Heart Carbs Merch: https://eriktheelectric.shop/collections/tank-tops/products/i-3-carbs-tank-top\nBurger Shirt: https://eriktheelectric.shop/products/burger-allover-t-shirt-2\n\n\nWatch Me Eat The World's Heaviest Woman's Diet: https://youtu.be/QrtWMm1dxbY\nWatch Me Eat 100,000 Calories: https://youtu.be/OSK4fPC966s\n\nWatch More Erik TheElectric:\nCheat Days: https://youtube.com/playlist?list=PLEutRp8s1YDjTOlEXWa-tCw9B2YphwzjO\nRestaurant Challenges: https://youtube.com/playlist?list=PLEutRp8s1YDg88pWYAlnX_tnpL9yvIhC5\nPopular Videos: https://www.youtube.com/playlist?list=PLEutRp8s1YDjcT2LlO0NA-_AjJ4ZLpUS2\nLatest Uploads: https://www.youtube.com/playlist?list=PLEutRp8s1YDjjOfhb-poGHsykhkZyrEKw\n\n\nErik’s 2nd Channel:   @TheElectrics \nErik’s 3rd Channel:     @ElectricTalks \n\n#foodchallenge #caloriechallenge \n\nI'm Erik. \nI specialize in high calorie food challenges. Whether it’s a massive 10,000 calorie burger, or it’s me attempting to eat the entire menu of a fast food restaurant. My goal is always to succeed in whatever food adventure I find myself in. \nIf you love calorie packed food challenges, be sure to follow along and subscribe to the channel!",
                    "2024-06-09T00:51:03Z",
                    "ErikTheElectric",
                    "https://yt3.ggpht.com/ytc/AIdro_nDTKq9EfTx0SA4Tv4FV9MfCh7zLXl0p4m2CjBwVyVybs4=s240-c-k-c0x00ffffff-no-rj",
                    channelId = "UC6huXz0F6-7KA7-mW0jdejA",
                    thumbnailsEntity = ThumbnailsEntity(
                        ThumbnailAttributesEntity(
                            "https://i.ytimg.com/vi/D7UqN8_kTpg/mqdefault.jpg",
                            180,
                            320,
                            "D7UqN8_kTpg",
                            "D7UqN8_kTpg"
                        ),
                        "D7UqN8_kTpg",
                        "D7UqN8_kTpg"
                    ),
                    "D7UqN8_kTpg",
                    "D7UqN8_kTpg"
                ),
                statistics = VideoStatisticsEntity(
                    977767,
                    53022,
                    "D7UqN8_kTpg",
                    "D7UqN8_kTpg"
                ),
                contentDetailsEntity = ContentDetailsEntity(
                    "PT33M20S",
                    "D7UqN8_kTpg",
                    "D7UqN8_kTpg"
                ),
                testDateTime
            ),

            YoutubeVideoEntity(
                id = "acxSH1A536Q",
                INITIAL_PAGE_TOKEN,
                snippet = VideoSnippetEntity(
                    "Hombre al Agua - RIP ANKHAL",
                    "Hombre al agua... #RIPANKHAL #Tiraera",
                    "2024-06-08T23:41:27Z",
                    "Noriel",
                    "https://yt3.ggpht.com/MamDoeUwqyGIowFfBJ4teXOuHSuwpUdSwv_EcanSB99-VIKxExaoEpm80qwEo5uiaGJCYXuGiw=s240-c-k-c0x00ffffff-no-nd-rj",
                    channelId = "UCLDAM_6WwevNIPHz7IalMLw",
                    thumbnailsEntity = ThumbnailsEntity(
                        ThumbnailAttributesEntity(
                            "https://i.ytimg.com/vi/acxSH1A536Q/mqdefault.jpg",
                            180,
                            320,
                            "acxSH1A536Q",
                            "acxSH1A536Q"
                        ),
                        "acxSH1A536Q",
                        "acxSH1A536Q"
                    ),
                    "acxSH1A536Q",
                    "acxSH1A536Q"
                ),
                statistics = VideoStatisticsEntity(
                    733513,
                    100876,
                    "acxSH1A536Q",
                    "acxSH1A536Q"
                ),
                contentDetailsEntity = ContentDetailsEntity(
                    "PT4M51S",
                    "acxSH1A536Q",
                    "acxSH1A536Q"
                ),
                testDateTime
            ),

            YoutubeVideoEntity(
                id = "Q9rlqktHiF0",
                INITIAL_PAGE_TOKEN,
                snippet = VideoSnippetEntity(
                    "I'm Running Out Of Time..",
                    "SUBSCRIBE NOW\nIn this DanTDM Minecraft video, I'm running out of time between now and the new Minecraft Tricky Trials Update\n\nEdited by: Me, DanTDM\n\nTwitter: http://www.twitter.com/DanTDM\nInstagram: http://www.instagram.com/DanTDM",
                    "2024-06-08T17:00:16Z",
                    "DanTDM",
                    "https://yt3.ggpht.com/_SQKtbdn1Xv6vkV_OX5D1jTNQY2SAQ5Gq-WSPlTbc8h5qMHQ3dnwpSXlpq8ADikltH3zC9KC5A=s240-c-k-c0x00ffffff-no-rj",
                    channelId = "UCS5Oz6CHmeoF7vSad0qqXfw",
                    thumbnailsEntity = ThumbnailsEntity(
                        ThumbnailAttributesEntity(
                            "https://i.ytimg.com/vi/Q9rlqktHiF0/mqdefault.jpg",
                            180,
                            320,
                            "Q9rlqktHiF0",
                            "Q9rlqktHiF0"
                        ),
                        "Q9rlqktHiF0",
                        "Q9rlqktHiF0"
                    ),
                    "Q9rlqktHiF0",
                    "Q9rlqktHiF0"
                ),
                statistics = VideoStatisticsEntity(
                    1666637,
                    86977,
                    "Q9rlqktHiF0",
                    "Q9rlqktHiF0"
                ),
                contentDetailsEntity = ContentDetailsEntity(
                    "PT42M4S",
                    "Q9rlqktHiF0",
                    "Q9rlqktHiF0"
                ),
                testDateTime
            )
        )

        val pageEntity = PageEntity(
            nextPageToken = "CAoQAA",
            currentPageToken = INITIAL_PAGE_TOKEN,
            prevPageToken = "CAUQAQ",
        )

        val TEST_DATABASE_VIDEO_RESPONSE = YoutubeVideoResponseEntity(
            pageEntity,
            TEST_DATABASE_VIDEO_LIST
        )
    }
}