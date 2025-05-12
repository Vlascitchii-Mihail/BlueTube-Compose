package com.vlascitchii.presentation_common.entity.videos

data class YoutubeVideoResponseUiModel(
    val nextPageToken: String? = null,
    var currentPageToken: String = "",
    val prevPageToken: String? = null,
    val items: List<YoutubeVideoUiModel> = emptyList()
) {

//    companion object {
////        private val RESPONSE_VIDEO_LIST = listOf(
//        private val VIDEO_LIST_NO_CHANNEL_IMG_URL = listOf(
//            YoutubeVideo(
//                id = "bbM6aSB6iMQ",
//                "",
//                snippet = VideoSnippet(
//                    "State of Origin 2024 | Blues v Maroons | Match Highlights",
//                    "The New South Wales Blues and the Queensland Maroons face off in Game 1 of the 2024 Ampol State of Origin series\n\n🏉 SUBSCRIBE FOR MORE NRL ACTION 🏉\nhttps://www.youtube.com/channel/UC33-OkQ6VCYk8xtml8Pk4-g?sub_confirmation=1\n\n🏉 Subscribe to the official NRLW Channel 🏉https://www.youtube.com/channel/UCME0EifJ3Xm3mGmLdBz1Kyw?sub_confirmation=1\n\nTo keep up to date with all the latest NRL content head to:\nNRL Watch: https://www.nrl.com/watch/\nNRL Tickets: https://tickets.nrl.com/\nNRL Draw: https://www.nrl.com/draw/",
//                    "2024-06-05T12:22:55Z",
//                    "NRL - National Rugby League",
//                    "",
//                    channelId = "UC33-OkQ6VCYk8xtml8Pk4-g",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/bbM6aSB6iMQ/mqdefault.jpg",
//                            180,
//                            320
//                        )
//                    )
//                ),
//                statistics = VideoStatistics(
//                    400242,
//                    3593
//                ),
//                contentDetails = ContentDetails(
//                    "PT5M"
//                )
//            ),
//
//            YoutubeVideo(
//                id = "990CmzkhEq4",
//                "",
//                snippet = VideoSnippet(
//                    "I Tried The Best Taco In The World",
//                    "It took a lot of tacos to get here.\n\nGet My Cookbook: https://bit.ly/TextureOverTaste\n\nAdditional Cookbook Options (other stores, international, etc.): https://bit.ly/WeissmanCookbook\n\nFOLLOW ME:\nInstagram: https://www.instagram.com/joshuaweissman\nTik Tok: https://www.tiktok.com/@flakeysalt\nTwitter: https://twitter.com/therealweissman\nFacebook: https://www.facebook.com/thejoshuaweissman\nSubreddit: https://www.reddit.com/r/JoshuaWeissman/\n---------------------------------------------------------------",
//                    "2024-06-09T14:30:03Z",
//                    "Joshua Weissman",
//                    "",
//                    channelId = "UChBEbMKI1eCcejTtmI32UEw",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/990CmzkhEq4/mqdefault.jpg",
//                            180,
//                            320
//                        )
//                    )
//                ),
//                statistics = VideoStatistics(
//                    698144,
//                    25698
//                ),
//                contentDetails = ContentDetails(
//                    "PT30M38S"
//                )
//            ),
//
//            YoutubeVideo(
//                id = "D7UqN8_kTpg",
//                "",
//                snippet = VideoSnippet(
//                    "I Finished The ENTIRE Menu Everywhere My Tesla Took Me for a Week",
//                    "Found out my tesla had a hungry button, this is what happened next.. \n\nSubscribe: https://www.youtube.com/c/ErikTheElectric?sub_confirmation=1 \n\n\nGet I Heart Carbs Merch: https://eriktheelectric.shop/collections/tank-tops/products/i-3-carbs-tank-top\nBurger Shirt: https://eriktheelectric.shop/products/burger-allover-t-shirt-2\n\n\nWatch Me Eat The World's Heaviest Woman's Diet: https://youtu.be/QrtWMm1dxbY\nWatch Me Eat 100,000 Calories: https://youtu.be/OSK4fPC966s\n\nWatch More Erik TheElectric:\nCheat Days: https://youtube.com/playlist?list=PLEutRp8s1YDjTOlEXWa-tCw9B2YphwzjO\nRestaurant Challenges: https://youtube.com/playlist?list=PLEutRp8s1YDg88pWYAlnX_tnpL9yvIhC5\nPopular Videos: https://www.youtube.com/playlist?list=PLEutRp8s1YDjcT2LlO0NA-_AjJ4ZLpUS2\nLatest Uploads: https://www.youtube.com/playlist?list=PLEutRp8s1YDjjOfhb-poGHsykhkZyrEKw\n\n\nErik’s 2nd Channel:   @TheElectrics \nErik’s 3rd Channel:     @ElectricTalks \n\n#foodchallenge #caloriechallenge \n\nI'm Erik. \nI specialize in high calorie food challenges. Whether it’s a massive 10,000 calorie burger, or it’s me attempting to eat the entire menu of a fast food restaurant. My goal is always to succeed in whatever food adventure I find myself in. \nIf you love calorie packed food challenges, be sure to follow along and subscribe to the channel!",
//                    "2024-06-09T00:51:03Z",
//                    "ErikTheElectric",
//                    "",
//                    channelId = "UC6huXz0F6-7KA7-mW0jdejA",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/D7UqN8_kTpg/mqdefault.jpg",
//                            180,
//                            320
//                        )
//                    )
//                ),
//                statistics = VideoStatistics(
//                    977767,
//                    53022
//                ),
//                contentDetails = ContentDetails(
//                    "PT33M20S"
//                )
//            ),
//
//            YoutubeVideo(
//                id = "acxSH1A536Q",
//                "",
//                snippet = VideoSnippet(
//                    "Hombre al Agua - RIP ANKHAL",
//                    "Hombre al agua... #RIPANKHAL #Tiraera",
//                    "2024-06-08T23:41:27Z",
//                    "Noriel",
//                    "",
//                    channelId = "UCLDAM_6WwevNIPHz7IalMLw",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/acxSH1A536Q/mqdefault.jpg",
//                            180,
//                            320
//                        )
//                    )
//                ),
//                statistics = VideoStatistics(
//                    733513,
//                    100876
//                ),
//                contentDetails = ContentDetails(
//                    "PT4M51S"
//                )
//            ),
//
//            YoutubeVideo(
//                id = "Q9rlqktHiF0",
//                "",
//                snippet = VideoSnippet(
//                    "I'm Running Out Of Time..",
//                    "SUBSCRIBE NOW\nIn this DanTDM Minecraft video, I'm running out of time between now and the new Minecraft Tricky Trials Update\n\nEdited by: Me, DanTDM\n\nTwitter: http://www.twitter.com/DanTDM\nInstagram: http://www.instagram.com/DanTDM",
//                    "2024-06-08T17:00:16Z",
//                    "DanTDM",
//                    "",
//                    channelId = "UCS5Oz6CHmeoF7vSad0qqXfw",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/Q9rlqktHiF0/mqdefault.jpg",
//                            180,
//                            320
//                        )
//                    )
//                ),
//                statistics = VideoStatistics(
//                    1666637,
//                    86977
//                ),
//                contentDetails = ContentDetails(
//                    "PT42M4S"
//                )
//            )
//        )
//
//        // val DEFAULT_VIDEO_RESPONSE_ = YoutubeVideoResponseUiModel(
//        val RESPONSE_VIDEO_LIST_NO_CHANNEL_IMG_URL = YoutubeVideoResponseUiModel(
//            nextPageToken = "CAoQAA",
//            "",
//            prevPageToken = "CAUQAQ",
//            VIDEO_LIST_NO_CHANNEL_IMG_URL
//        )


//RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG_URL
//        val VIDEO_LIST_WITH_CHANNEL_IMG_URL = listOf(
//            YoutubeVideo(
//                id = "bbM6aSB6iMQ",
//                "",
//                snippet = VideoSnippet(
//                    "State of Origin 2024 | Blues v Maroons | Match Highlights",
//                    "The New South Wales Blues and the Queensland Maroons face off in Game 1 of the 2024 Ampol State of Origin series\n\n🏉 SUBSCRIBE FOR MORE NRL ACTION 🏉\nhttps://www.youtube.com/channel/UC33-OkQ6VCYk8xtml8Pk4-g?sub_confirmation=1\n\n🏉 Subscribe to the official NRLW Channel 🏉https://www.youtube.com/channel/UCME0EifJ3Xm3mGmLdBz1Kyw?sub_confirmation=1\n\nTo keep up to date with all the latest NRL content head to:\nNRL Watch: https://www.nrl.com/watch/\nNRL Tickets: https://tickets.nrl.com/\nNRL Draw: https://www.nrl.com/draw/",
//                    "2024-06-05T12:22:55Z",
//                    "NRL - National Rugby League",
//                    "https://yt3.ggpht.com/Pui0xFjDp_F-x0UK5Rkfh1tGkwZLy7fkU4Pr44Zht3DJcMwWXLnxoVIGSRS5Pr7SVxgES0-ouA=s240-c-k-c0x00ffffff-no-rj",
//                    channelId = "UC33-OkQ6VCYk8xtml8Pk4-g",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/bbM6aSB6iMQ/mqdefault.jpg",
//                            180,
//                            320
//                        )
//                    )
//                ),
//                statistics = VideoStatistics(
//                    400242,
//                    3593
//                ),
//                contentDetails = ContentDetails(
//                    "PT5M"
//                )
//            ),
//
//            YoutubeVideo(
//                id = "990CmzkhEq4",
//                "",
//                snippet = VideoSnippet(
//                    "I Tried The Best Taco In The World",
//                    "It took a lot of tacos to get here.\n\nGet My Cookbook: https://bit.ly/TextureOverTaste\n\nAdditional Cookbook Options (other stores, international, etc.): https://bit.ly/WeissmanCookbook\n\nFOLLOW ME:\nInstagram: https://www.instagram.com/joshuaweissman\nTik Tok: https://www.tiktok.com/@flakeysalt\nTwitter: https://twitter.com/therealweissman\nFacebook: https://www.facebook.com/thejoshuaweissman\nSubreddit: https://www.reddit.com/r/JoshuaWeissman/\n---------------------------------------------------------------",
//                    "2024-06-09T14:30:03Z",
//                    "Joshua Weissman",
//                    "https://yt3.ggpht.com/ytc/AIdro_nfXRvoxu5cFt2H4WhJfFLbL5SVdzmvEnFymnPzH3_1qPM=s240-c-k-c0x00ffffff-no-rj",
//                    channelId = "UChBEbMKI1eCcejTtmI32UEw",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/990CmzkhEq4/mqdefault.jpg",
//                            180,
//                            320
//                        )
//                    )
//                ),
//                statistics = VideoStatistics(
//                    698144,
//                    25698
//                ),
//                contentDetails = ContentDetails(
//                    "PT30M38S"
//                )
//            ),
//
//            YoutubeVideo(
//                id = "D7UqN8_kTpg",
//                "",
//                snippet = VideoSnippet(
//                    "I Finished The ENTIRE Menu Everywhere My Tesla Took Me for a Week",
//                    "Found out my tesla had a hungry button, this is what happened next.. \n\nSubscribe: https://www.youtube.com/c/ErikTheElectric?sub_confirmation=1 \n\n\nGet I Heart Carbs Merch: https://eriktheelectric.shop/collections/tank-tops/products/i-3-carbs-tank-top\nBurger Shirt: https://eriktheelectric.shop/products/burger-allover-t-shirt-2\n\n\nWatch Me Eat The World's Heaviest Woman's Diet: https://youtu.be/QrtWMm1dxbY\nWatch Me Eat 100,000 Calories: https://youtu.be/OSK4fPC966s\n\nWatch More Erik TheElectric:\nCheat Days: https://youtube.com/playlist?list=PLEutRp8s1YDjTOlEXWa-tCw9B2YphwzjO\nRestaurant Challenges: https://youtube.com/playlist?list=PLEutRp8s1YDg88pWYAlnX_tnpL9yvIhC5\nPopular Videos: https://www.youtube.com/playlist?list=PLEutRp8s1YDjcT2LlO0NA-_AjJ4ZLpUS2\nLatest Uploads: https://www.youtube.com/playlist?list=PLEutRp8s1YDjjOfhb-poGHsykhkZyrEKw\n\n\nErik’s 2nd Channel:   @TheElectrics \nErik’s 3rd Channel:     @ElectricTalks \n\n#foodchallenge #caloriechallenge \n\nI'm Erik. \nI specialize in high calorie food challenges. Whether it’s a massive 10,000 calorie burger, or it’s me attempting to eat the entire menu of a fast food restaurant. My goal is always to succeed in whatever food adventure I find myself in. \nIf you love calorie packed food challenges, be sure to follow along and subscribe to the channel!",
//                    "2024-06-09T00:51:03Z",
//                    "ErikTheElectric",
//                    "https://yt3.ggpht.com/ytc/AIdro_nDTKq9EfTx0SA4Tv4FV9MfCh7zLXl0p4m2CjBwVyVybs4=s240-c-k-c0x00ffffff-no-rj",
//                    channelId = "UC6huXz0F6-7KA7-mW0jdejA",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/D7UqN8_kTpg/mqdefault.jpg",
//                            180,
//                            320
//                        )
//                    )
//                ),
//                statistics = VideoStatistics(
//                    977767,
//                    53022
//                ),
//                contentDetails = ContentDetails(
//                    "PT33M20S"
//                )
//            ),
//
//            YoutubeVideo(
//                id = "acxSH1A536Q",
//                "",
//                snippet = VideoSnippet(
//                    "Hombre al Agua - RIP ANKHAL",
//                    "Hombre al agua... #RIPANKHAL #Tiraera",
//                    "2024-06-08T23:41:27Z",
//                    "Noriel",
//                    "https://yt3.ggpht.com/MamDoeUwqyGIowFfBJ4teXOuHSuwpUdSwv_EcanSB99-VIKxExaoEpm80qwEo5uiaGJCYXuGiw=s240-c-k-c0x00ffffff-no-nd-rj",
//                    channelId = "UCLDAM_6WwevNIPHz7IalMLw",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/acxSH1A536Q/mqdefault.jpg",
//                            180,
//                            320
//                        )
//                    )
//                ),
//                statistics = VideoStatistics(
//                    733513,
//                    100876
//                ),
//                contentDetails = ContentDetails(
//                    "PT4M51S"
//                )
//            ),
//
//            YoutubeVideo(
//                id = "Q9rlqktHiF0",
//                "",
//                snippet = VideoSnippet(
//                    "I'm Running Out Of Time..",
//                    "SUBSCRIBE NOW\nIn this DanTDM Minecraft video, I'm running out of time between now and the new Minecraft Tricky Trials Update\n\nEdited by: Me, DanTDM\n\nTwitter: http://www.twitter.com/DanTDM\nInstagram: http://www.instagram.com/DanTDM",
//                    "2024-06-08T17:00:16Z",
//                    "DanTDM",
//                    "https://yt3.ggpht.com/_SQKtbdn1Xv6vkV_OX5D1jTNQY2SAQ5Gq-WSPlTbc8h5qMHQ3dnwpSXlpq8ADikltH3zC9KC5A=s240-c-k-c0x00ffffff-no-rj",
//                    channelId = "UCS5Oz6CHmeoF7vSad0qqXfw",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/Q9rlqktHiF0/mqdefault.jpg",
//                            180,
//                            320
//                        )
//                    )
//                ),
//                statistics = VideoStatistics(
//                    1666637,
//                    86977
//                ),
//                contentDetails = ContentDetails(
//                    "PT42M4S"
//                )
//            )
//        )
//
////DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
//        val RESPONSE_VIDEO_LIST_WITH_CHANNEL_IMG = YoutubeVideoResponseUiModel(
//            nextPageToken = "CAoQAA",
//            "",
//            prevPageToken = "CAUQAQ",
//            VIDEO_LIST_WITH_CHANNEL_IMG_URL
//        )
//
//
//        val testDateTime: OffsetDateTime = OffsetDateTime.now()
//        const val INITIAL_PAGE_TOKEN = "Initial page"

////TODO: compare with previous lists and delete this one if previous can be used for testing the database
//        val TEST_DATABASE_VIDEO_LIST = listOf(
//            YoutubeVideo(
//                id = "bbM6aSB6iMQ",
//                INITIAL_PAGE_TOKEN,
//                snippet = VideoSnippet(
//                    "State of Origin 2024 | Blues v Maroons | Match Highlights",
//                    "The New South Wales Blues and the Queensland Maroons face off in Game 1 of the 2024 Ampol State of Origin series\n\n🏉 SUBSCRIBE FOR MORE NRL ACTION 🏉\nhttps://www.youtube.com/channel/UC33-OkQ6VCYk8xtml8Pk4-g?sub_confirmation=1\n\n🏉 Subscribe to the official NRLW Channel 🏉https://www.youtube.com/channel/UCME0EifJ3Xm3mGmLdBz1Kyw?sub_confirmation=1\n\nTo keep up to date with all the latest NRL content head to:\nNRL Watch: https://www.nrl.com/watch/\nNRL Tickets: https://tickets.nrl.com/\nNRL Draw: https://www.nrl.com/draw/",
//                    "2024-06-05T12:22:55Z",
//                    "NRL - National Rugby League",
//                    "https://yt3.ggpht.com/Pui0xFjDp_F-x0UK5Rkfh1tGkwZLy7fkU4Pr44Zht3DJcMwWXLnxoVIGSRS5Pr7SVxgES0-ouA=s240-c-k-c0x00ffffff-no-rj",
//                    channelId = "UC33-OkQ6VCYk8xtml8Pk4-g",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/bbM6aSB6iMQ/mqdefault.jpg",
//                            180,
//                            320,
//                            "bbM6aSB6iMQ",
//                            "bbM6aSB6iMQ"
//                        ),
//                        "bbM6aSB6iMQ",
//                        "bbM6aSB6iMQ"
//                    ),
//                    "bbM6aSB6iMQ",
//                    "bbM6aSB6iMQ"
//                ),
//                statistics = VideoStatistics(
//                    400242,
//                    3593,
//                    "bbM6aSB6iMQ",
//                    "bbM6aSB6iMQ"
//                ),
//                contentDetails = ContentDetails(
//                    "PT5M",
//                    "bbM6aSB6iMQ",
//                    "bbM6aSB6iMQ"
//                ),
//                testDateTime
//            ),
//
//            YoutubeVideo(
//                id = "990CmzkhEq4",
//                INITIAL_PAGE_TOKEN,
//                snippet = VideoSnippet(
//                    "I Tried The Best Taco In The World",
//                    "It took a lot of tacos to get here.\n\nGet My Cookbook: https://bit.ly/TextureOverTaste\n\nAdditional Cookbook Options (other stores, international, etc.): https://bit.ly/WeissmanCookbook\n\nFOLLOW ME:\nInstagram: https://www.instagram.com/joshuaweissman\nTik Tok: https://www.tiktok.com/@flakeysalt\nTwitter: https://twitter.com/therealweissman\nFacebook: https://www.facebook.com/thejoshuaweissman\nSubreddit: https://www.reddit.com/r/JoshuaWeissman/\n---------------------------------------------------------------",
//                    "2024-06-09T14:30:03Z",
//                    "Joshua Weissman",
//                    "https://yt3.ggpht.com/ytc/AIdro_nfXRvoxu5cFt2H4WhJfFLbL5SVdzmvEnFymnPzH3_1qPM=s240-c-k-c0x00ffffff-no-rj",
//                    channelId = "UChBEbMKI1eCcejTtmI32UEw",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/990CmzkhEq4/mqdefault.jpg",
//                            180,
//                            320,
//                            "990CmzkhEq4",
//                            "990CmzkhEq4"
//                        ),
//                        "990CmzkhEq4",
//                        "990CmzkhEq4"
//                    ),
//                    "990CmzkhEq4",
//                    "990CmzkhEq4"
//                ),
//                statistics = VideoStatistics(
//                    698144,
//                    25698,
//                    "990CmzkhEq4",
//                    "990CmzkhEq4"
//                ),
//                contentDetails = ContentDetails(
//                    "PT30M38S",
//                    "990CmzkhEq4",
//                    "990CmzkhEq4"
//                ),
//                testDateTime
//            ),
//
//            YoutubeVideo(
//                id = "D7UqN8_kTpg",
//                INITIAL_PAGE_TOKEN,
//                snippet = VideoSnippet(
//                    "I Finished The ENTIRE Menu Everywhere My Tesla Took Me for a Week",
//                    "Found out my tesla had a hungry button, this is what happened next.. \n\nSubscribe: https://www.youtube.com/c/ErikTheElectric?sub_confirmation=1 \n\n\nGet I Heart Carbs Merch: https://eriktheelectric.shop/collections/tank-tops/products/i-3-carbs-tank-top\nBurger Shirt: https://eriktheelectric.shop/products/burger-allover-t-shirt-2\n\n\nWatch Me Eat The World's Heaviest Woman's Diet: https://youtu.be/QrtWMm1dxbY\nWatch Me Eat 100,000 Calories: https://youtu.be/OSK4fPC966s\n\nWatch More Erik TheElectric:\nCheat Days: https://youtube.com/playlist?list=PLEutRp8s1YDjTOlEXWa-tCw9B2YphwzjO\nRestaurant Challenges: https://youtube.com/playlist?list=PLEutRp8s1YDg88pWYAlnX_tnpL9yvIhC5\nPopular Videos: https://www.youtube.com/playlist?list=PLEutRp8s1YDjcT2LlO0NA-_AjJ4ZLpUS2\nLatest Uploads: https://www.youtube.com/playlist?list=PLEutRp8s1YDjjOfhb-poGHsykhkZyrEKw\n\n\nErik’s 2nd Channel:   @TheElectrics \nErik’s 3rd Channel:     @ElectricTalks \n\n#foodchallenge #caloriechallenge \n\nI'm Erik. \nI specialize in high calorie food challenges. Whether it’s a massive 10,000 calorie burger, or it’s me attempting to eat the entire menu of a fast food restaurant. My goal is always to succeed in whatever food adventure I find myself in. \nIf you love calorie packed food challenges, be sure to follow along and subscribe to the channel!",
//                    "2024-06-09T00:51:03Z",
//                    "ErikTheElectric",
//                    "https://yt3.ggpht.com/ytc/AIdro_nDTKq9EfTx0SA4Tv4FV9MfCh7zLXl0p4m2CjBwVyVybs4=s240-c-k-c0x00ffffff-no-rj",
//                    channelId = "UC6huXz0F6-7KA7-mW0jdejA",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/D7UqN8_kTpg/mqdefault.jpg",
//                            180,
//                            320,
//                            "D7UqN8_kTpg",
//                            "D7UqN8_kTpg"
//                        ),
//                        "D7UqN8_kTpg",
//                        "D7UqN8_kTpg"
//                    ),
//                    "D7UqN8_kTpg",
//                    "D7UqN8_kTpg"
//                ),
//                statistics = VideoStatistics(
//                    977767,
//                    53022,
//                    "D7UqN8_kTpg",
//                    "D7UqN8_kTpg"
//                ),
//                contentDetails = ContentDetails(
//                    "PT33M20S",
//                    "D7UqN8_kTpg",
//                    "D7UqN8_kTpg"
//                ),
//                testDateTime
//            ),
//
//            YoutubeVideo(
//                id = "acxSH1A536Q",
//                INITIAL_PAGE_TOKEN,
//                snippet = VideoSnippet(
//                    "Hombre al Agua - RIP ANKHAL",
//                    "Hombre al agua... #RIPANKHAL #Tiraera",
//                    "2024-06-08T23:41:27Z",
//                    "Noriel",
//                    "https://yt3.ggpht.com/MamDoeUwqyGIowFfBJ4teXOuHSuwpUdSwv_EcanSB99-VIKxExaoEpm80qwEo5uiaGJCYXuGiw=s240-c-k-c0x00ffffff-no-nd-rj",
//                    channelId = "UCLDAM_6WwevNIPHz7IalMLw",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/acxSH1A536Q/mqdefault.jpg",
//                            180,
//                            320,
//                            "acxSH1A536Q",
//                            "acxSH1A536Q"
//                        ),
//                        "acxSH1A536Q",
//                        "acxSH1A536Q"
//                    ),
//                    "acxSH1A536Q",
//                    "acxSH1A536Q"
//                ),
//                statistics = VideoStatistics(
//                    733513,
//                    100876,
//                    "acxSH1A536Q",
//                    "acxSH1A536Q"
//                ),
//                contentDetails = ContentDetails(
//                    "PT4M51S",
//                    "acxSH1A536Q",
//                    "acxSH1A536Q"
//                ),
//                testDateTime
//            ),
//
//            YoutubeVideo(
//                id = "Q9rlqktHiF0",
//                INITIAL_PAGE_TOKEN,
//                snippet = VideoSnippet(
//                    "I'm Running Out Of Time..",
//                    "SUBSCRIBE NOW\nIn this DanTDM Minecraft video, I'm running out of time between now and the new Minecraft Tricky Trials Update\n\nEdited by: Me, DanTDM\n\nTwitter: http://www.twitter.com/DanTDM\nInstagram: http://www.instagram.com/DanTDM",
//                    "2024-06-08T17:00:16Z",
//                    "DanTDM",
//                    "https://yt3.ggpht.com/_SQKtbdn1Xv6vkV_OX5D1jTNQY2SAQ5Gq-WSPlTbc8h5qMHQ3dnwpSXlpq8ADikltH3zC9KC5A=s240-c-k-c0x00ffffff-no-rj",
//                    channelId = "UCS5Oz6CHmeoF7vSad0qqXfw",
//                    thumbnailsUiModel = ThumbnailsUiModel(
//                        ThumbnailAttributesUiModel(
//                            "https://i.ytimg.com/vi/Q9rlqktHiF0/mqdefault.jpg",
//                            180,
//                            320,
//                            "Q9rlqktHiF0",
//                            "Q9rlqktHiF0"
//                        ),
//                        "Q9rlqktHiF0",
//                        "Q9rlqktHiF0"
//                    ),
//                    "Q9rlqktHiF0",
//                    "Q9rlqktHiF0"
//                ),
//                statistics = VideoStatistics(
//                    1666637,
//                    86977,
//                    "Q9rlqktHiF0",
//                    "Q9rlqktHiF0"
//                ),
//                contentDetails = ContentDetails(
//                    "PT42M4S",
//                    "Q9rlqktHiF0",
//                    "Q9rlqktHiF0"
//                ),
//                testDateTime
//            )
//        )
//
//        val TEST_DATABASE_VIDEO_RESPONSE = YoutubeVideoResponseUiModel(
//            nextPageToken = "CAoQAA",
//            INITIAL_PAGE_TOKEN,
//            prevPageToken = "CAUQAQ",
//            TEST_DATABASE_VIDEO_LIST
//        )
//    }
}