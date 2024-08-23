package com.appelier.bluetubecompose.search_video.model

import com.appelier.bluetubecompose.screen_video_list.model.ThumbnailAttributes
import com.appelier.bluetubecompose.screen_video_list.model.Thumbnails
import com.appelier.bluetubecompose.screen_video_list.model.videos.VideoSnippet
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchVideoResponse (
    val nextPageToken: String = "",
    val prevPageToken: String? = null,
    val items: List<SearchVideoItem> = emptyList()
)

private val RESPONSE_SEARCH_VIDEO_LIST = listOf(
    SearchVideoItem(
        SearchVideoItemId(videoId = "bbM6aSB6iMQ"),
        snippet = VideoSnippet(
            "State of Origin 2024 | Blues v Maroons | Match Highlights",
            "The New South Wales Blues and the Queensland Maroons face off in Game 1 of the 2024 Ampol State of Origin series\n\n🏉 SUBSCRIBE FOR MORE NRL ACTION 🏉\nhttps://www.youtube.com/channel/UC33-OkQ6VCYk8xtml8Pk4-g?sub_confirmation=1\n\n🏉 Subscribe to the official NRLW Channel 🏉https://www.youtube.com/channel/UCME0EifJ3Xm3mGmLdBz1Kyw?sub_confirmation=1\n\nTo keep up to date with all the latest NRL content head to:\nNRL Watch: https://www.nrl.com/watch/\nNRL Tickets: https://tickets.nrl.com/\nNRL Draw: https://www.nrl.com/draw/",
            "2024-06-05T12:22:55Z",
            "NRL - National Rugby League",
            "",
            channelId = "UC33-OkQ6VCYk8xtml8Pk4-g",
            thumbnails = Thumbnails(
                ThumbnailAttributes(
                    "https://i.ytimg.com/vi/bbM6aSB6iMQ/mqdefault.jpg",
                    180,
                    320
                )
            )
        )
    ),

    SearchVideoItem(
        SearchVideoItemId(videoId = "990CmzkhEq4"),
        snippet = VideoSnippet(
            "I Tried The Best Taco In The World",
            "It took a lot of tacos to get here.\n\nGet My Cookbook: https://bit.ly/TextureOverTaste\n\nAdditional Cookbook Options (other stores, international, etc.): https://bit.ly/WeissmanCookbook\n\nFOLLOW ME:\nInstagram: https://www.instagram.com/joshuaweissman\nTik Tok: https://www.tiktok.com/@flakeysalt\nTwitter: https://twitter.com/therealweissman\nFacebook: https://www.facebook.com/thejoshuaweissman\nSubreddit: https://www.reddit.com/r/JoshuaWeissman/\n---------------------------------------------------------------",
            "2024-06-09T14:30:03Z",
            "Joshua Weissman",
            "",
            channelId = "UChBEbMKI1eCcejTtmI32UEw",
            thumbnails = Thumbnails(
                ThumbnailAttributes(
                    "https://i.ytimg.com/vi/990CmzkhEq4/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    ),
    SearchVideoItem(
        SearchVideoItemId(videoId = "D7UqN8_kTpg"),
        snippet = VideoSnippet(
            "I Finished The ENTIRE Menu Everywhere My Tesla Took Me for a Week",
            "Found out my tesla had a hungry button, this is what happened next.. \n\nSubscribe: https://www.youtube.com/c/ErikTheElectric?sub_confirmation=1 \n\n\nGet I Heart Carbs Merch: https://eriktheelectric.shop/collections/tank-tops/products/i-3-carbs-tank-top\nBurger Shirt: https://eriktheelectric.shop/products/burger-allover-t-shirt-2\n\n\nWatch Me Eat The World's Heaviest Woman's Diet: https://youtu.be/QrtWMm1dxbY\nWatch Me Eat 100,000 Calories: https://youtu.be/OSK4fPC966s\n\nWatch More Erik TheElectric:\nCheat Days: https://youtube.com/playlist?list=PLEutRp8s1YDjTOlEXWa-tCw9B2YphwzjO\nRestaurant Challenges: https://youtube.com/playlist?list=PLEutRp8s1YDg88pWYAlnX_tnpL9yvIhC5\nPopular Videos: https://www.youtube.com/playlist?list=PLEutRp8s1YDjcT2LlO0NA-_AjJ4ZLpUS2\nLatest Uploads: https://www.youtube.com/playlist?list=PLEutRp8s1YDjjOfhb-poGHsykhkZyrEKw\n\n\nErik’s 2nd Channel:   @TheElectrics \nErik’s 3rd Channel:     @ElectricTalks \n\n#foodchallenge #caloriechallenge \n\nI'm Erik. \nI specialize in high calorie food challenges. Whether it’s a massive 10,000 calorie burger, or it’s me attempting to eat the entire menu of a fast food restaurant. My goal is always to succeed in whatever food adventure I find myself in. \nIf you love calorie packed food challenges, be sure to follow along and subscribe to the channel!",
            "2024-06-09T00:51:03Z",
            "ErikTheElectric",
            "",
            channelId = "UC6huXz0F6-7KA7-mW0jdejA",
            thumbnails = Thumbnails(
                ThumbnailAttributes(
                    "https://i.ytimg.com/vi/D7UqN8_kTpg/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    ),

    SearchVideoItem(
        SearchVideoItemId(videoId = "acxSH1A536Q"),
        snippet = VideoSnippet(
            "Hombre al Agua - RIP ANKHAL",
            "Hombre al agua... #RIPANKHAL #Tiraera",
            "2024-06-08T23:41:27Z",
            "Noriel",
            "",
            channelId = "UCLDAM_6WwevNIPHz7IalMLw",
            thumbnails = Thumbnails(
                ThumbnailAttributes(
                    "https://i.ytimg.com/vi/acxSH1A536Q/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    ),

    SearchVideoItem(
        SearchVideoItemId(videoId = "Q9rlqktHiF0"),
        snippet = VideoSnippet(
            "I'm Running Out Of Time..",
            "SUBSCRIBE NOW\nIn this DanTDM Minecraft video, I'm running out of time between now and the new Minecraft Tricky Trials Update\n\nEdited by: Me, DanTDM\n\nTwitter: http://www.twitter.com/DanTDM\nInstagram: http://www.instagram.com/DanTDM",
            "2024-06-08T17:00:16Z",
            "DanTDM",
            "",
            channelId = "UCS5Oz6CHmeoF7vSad0qqXfw",
            thumbnails = Thumbnails(
                ThumbnailAttributes(
                    "https://i.ytimg.com/vi/Q9rlqktHiF0/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    )
)

val DEFAULT_SEARCH_VIDEO_RESPONSE = SearchVideoResponse(
    nextPageToken = "CAoQAA",
    prevPageToken = "CAUQAQ",
    RESPONSE_SEARCH_VIDEO_LIST
)


private val RESPONSE_SHORTS_LIST = listOf(
    SearchVideoItem(
        SearchVideoItemId(videoId = "MnLjVkNrJSE"),
        snippet = VideoSnippet(
            "This trio is the best☀\uFE0F #kdramaedit#extraordinaryyou#snowdropkdrama#kdramalover#asiandrama#kdramalo",
            "7055734468606283014.",
            "2022-06-11T06:00:40Z",
            "scene Kiss",
            "",
            channelId = "UCA_UEa12RqJ4E6aWodNtWGQ",
            thumbnails = Thumbnails(
                ThumbnailAttributes(
                    "https://i.ytimg.com/vi/MnLjVkNrJSE/mqdefault.jpg",
                    180,
                    320
                )
            )
        )
    ),

    SearchVideoItem(
        SearchVideoItemId(videoId = "hLX-_cNq4m0"),
        snippet = VideoSnippet(
            "上期答案：因为土妹根本就不是妈妈亲生的！#轻漫计划",
            "上期答案：因为土妹根本就不是妈妈亲生的！#轻漫计划.",
            "2022-06-22T10:00:48Z",
            "山菜同学",
            "",
            channelId = "UCuieJRU0ShA-GfquEyyN43w",
            thumbnails = Thumbnails(
                ThumbnailAttributes(
                    "https://i.ytimg.com/vi/hLX-_cNq4m0/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    ),
    SearchVideoItem(
        SearchVideoItemId(videoId = "7GJy9fFsMCo"),
        snippet = VideoSnippet(
            "上期答案：土妹体力太好了，把蜥蜴累垮了！#轻漫计划 #悬疑推理",
            "上期答案：土妹体力太好了，把蜥蜴累垮了！#轻漫计划#悬疑推理.",
            "2022-07-29T10:00:04Z",
            "山菜同学",
            "",
            channelId = "UCuieJRU0ShA-GfquEyyN43w",
            thumbnails = Thumbnails(
                ThumbnailAttributes(
                    "https://i.ytimg.com/vi/7GJy9fFsMCo/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    ),

    SearchVideoItem(
        SearchVideoItemId(videoId = "QVjVB89hMOc"),
        snippet = VideoSnippet(
            "上期答案：因为土妹开始了上晚班，并且再也没做过任何不健康的事情！",
            "上期答案：因为土妹开始了上晚班，并且再也没做过任何不健康的事情！",
            "2022-05-25T09:00:59Z",
            "山菜同学",
            "",
            channelId = "UCuieJRU0ShA-GfquEyyN43w",
            thumbnails = Thumbnails(
                ThumbnailAttributes(
                    "https://i.ytimg.com/vi/QVjVB89hMOc/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    ),

    SearchVideoItem(
        SearchVideoItemId(videoId = "VEOsr3FM7VM"),
        snippet = VideoSnippet(
            "上期答案：土妹的脚虽然结实，但是太臭了！#轻漫计划",
            "上期答案：土妹的脚虽然结实，但是太臭了！#轻漫计划.",
            "2022-07-12T09:00:37Z",
            "山菜同学",
            "",
            channelId = "UCuieJRU0ShA-GfquEyyN43w",
            thumbnails = Thumbnails(
                ThumbnailAttributes(
                    "https://i.ytimg.com/vi/VEOsr3FM7VM/mqdefault.jpg",
                    180,
                    320
                )
            )
        ),
    )
)

val DEFAULT_SHORTS_RESPONSE = SearchVideoResponse(
    nextPageToken = "CAoQAA",
    prevPageToken = "CAUQAQ",
    RESPONSE_SHORTS_LIST
)
