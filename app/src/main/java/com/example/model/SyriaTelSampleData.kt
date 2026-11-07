package com.example.model

import com.example.R

object SyriaTelSampleData {

    val promoBanners = listOf(
        PromoBanner(
            id = "banner_1",
            badge = BiText("عرض حصري 5G/4G+", "5G/4G+ Exclusive"),
            title = BiText("باقة سوبر نت 25 GB", "SuperNet 25 GB Package"),
            description = BiText(
                "ضاعف سرعتك واستمتع بإنترنت فائق السرعة طوال الشهر بخصم 30%",
                "Double your speed and enjoy ultra-fast internet all month with 30% off"
            ),
            ctaText = BiText("اشترك الآن", "Subscribe Now"),
            targetCategory = OfferCategory.INTERNET,
            accentHex = 0xFFC5131B,
            secondaryHex = 0xFF5D070C,
            imageRes = R.drawable.img_promo_internet
        ),
        PromoBanner(
            id = "banner_2",
            badge = BiText("باقة شاملة", "All-in-One Mix"),
            title = BiText("باقة ميكس الذهبية الشاملة", "Golden All-in-One Mix"),
            description = BiText(
                "إنترنت + دقائق + رسائل في باقة واحدة متكاملة تناسب جميع احتياجاتك",
                "Internet + Minutes + SMS combined in one seamless bundle for your needs"
            ),
            ctaText = BiText("اكتشف العرض", "Explore Offer"),
            targetCategory = OfferCategory.MIX,
            accentHex = 0xFF12804C,
            secondaryHex = 0xFF0A3924,
            imageRes = R.drawable.img_promo_mix
        ),
        PromoBanner(
            id = "banner_3",
            badge = BiText("دقائق بلا حدود", "Voice Minutes"),
            title = BiText("دقائق غير محدودة في عطلة الأسبوع", "Unlimited Weekend Minutes"),
            description = BiText(
                "تحدث مع عائلتك وأصدقائك بلا حدود على شبكة سيريتل طوال عطلة الأسبوع",
                "Talk endlessly with family and friends across SyriaTel all weekend long"
            ),
            ctaText = BiText("عرض الباقات", "View Bundles"),
            targetCategory = OfferCategory.MINUTES,
            accentHex = 0xFF9E1117,
            secondaryHex = 0xFF23272F,
            imageRes = R.drawable.img_promo_minutes
        ),
        PromoBanner(
            id = "banner_4",
            badge = BiText("باقات الرسائل", "SMS Bundles"),
            title = BiText("باقة 300 رسالة محلية شاملة", "300 All-Network SMS Pack"),
            description = BiText(
                "تواصل مع الجميع عبر الرسائل النصية لكافة الشبكات المحلية بأفضل الأسعار",
                "Stay connected with local SMS messages across all Syrian mobile networks"
            ),
            ctaText = BiText("تفعيل الباقة", "Activate Pack"),
            targetCategory = OfferCategory.SMS,
            accentHex = 0xFF1F4E79,
            secondaryHex = 0xFF132639,
            imageRes = R.drawable.img_promo_sms
        ),
        PromoBanner(
            id = "banner_5",
            badge = BiText("عروض VIP الخاصة", "VIP Special Offer"),
            title = BiText("عرض رقمك المميز بخصم 40%", "VIP Tailored Offer 40% Off"),
            description = BiText(
                "15 GB إنترنت + 500 دقيقة اتصال مصممة خصيصاً لنمط استهلاكك الشهري",
                "15 GB Data + 500 Voice Minutes crafted specifically for your monthly usage"
            ),
            ctaText = BiText("احصل على العرض", "Claim VIP Offer"),
            targetCategory = OfferCategory.SPECIAL,
            accentHex = 0xFF7D1118,
            secondaryHex = 0xFF281215,
            imageRes = R.drawable.img_promo_special
        ),
        PromoBanner(
            id = "banner_6",
            badge = BiText("هدية سنوية", "Anniversary Gift"),
            title = BiText("ذكرى سنوية خطك مع سيريتل", "Your SyriaTel Line Anniversary"),
            description = BiText(
                "شكراً لثقتك بنا! احصل على 5 GB مجانية و250 دقيقة ضمن الشبكة",
                "Thank you for your loyalty! Claim 5 GB free data and 250 on-net minutes"
            ),
            ctaText = BiText("فعّل هديتك", "Claim Gift"),
            targetCategory = OfferCategory.ANNIVERSARY,
            accentHex = 0xFF2B313B,
            secondaryHex = 0xFF8C141A,
            imageRes = R.drawable.img_promo_anniversary
        )
    )

    val allOffers = listOf(
        // 1. INTERNET
        OfferItem(
            id = "off_net_1",
            category = OfferCategory.INTERNET,
            name = BiText("باقة سيرف 10 GB الشهرية", "Surf 10 GB Monthly"),
            description = BiText(
                "باقة إنترنت عالية السرعة صالحة على مدار الساعة مع ترحيل الرصيد المتبقي.",
                "High-speed 24/7 internet package with data rollover support."
            ),
            quotaLabel = BiText("10 GB إنترنت", "10 GB Internet"),
            priceSyp = 18000,
            validity = BiText("30 يوماً", "30 Days"),
            highlightTag = BiText("الأكثر طلباً", "Most Popular"),
            internetBonusGb = 10f
        ),
        OfferItem(
            id = "off_net_2",
            category = OfferCategory.INTERNET,
            name = BiText("باقة التيربو 25 GB", "Turbo 25 GB Bundle"),
            description = BiText(
                "مثالية للبث المرئي والألعاب والتحميل السريع عبر شبكة 4G+.",
                "Ideal for HD video streaming, gaming, and fast 4G+ downloads."
            ),
            quotaLabel = BiText("25 GB إنترنت", "25 GB Internet"),
            priceSyp = 35000,
            validity = BiText("30 يوماً", "30 Days"),
            highlightTag = BiText("توفير 25%", "Save 25%"),
            internetBonusGb = 25f
        ),
        OfferItem(
            id = "off_net_3",
            category = OfferCategory.INTERNET,
            name = BiText("باقة سهر الليالي 6 GB", "Night Owl 6 GB"),
            description = BiText(
                "استمتع بالتصفح والتحميل من الساعة 12 ليلاً حتى 9 صباحاً بسعر مميز.",
                "Enjoy browsing and downloading from 12 AM to 9 AM at a special rate."
            ),
            quotaLabel = BiText("6 GB ليلي", "6 GB Night Data"),
            priceSyp = 7500,
            validity = BiText("15 يوماً", "15 Days"),
            highlightTag = BiText("اقتصادية", "Budget Pick"),
            internetBonusGb = 6f
        ),

        // 2. SMS
        OfferItem(
            id = "off_sms_1",
            category = OfferCategory.SMS,
            name = BiText("باقة 300 رسالة محلية", "300 Local SMS Bundle"),
            description = BiText(
                "رسائل نصية قصيرة لجميع الشبكات المحلية داخل سوريا.",
                "Local SMS messages valid across all Syrian mobile networks."
            ),
            quotaLabel = BiText("300 رسالة", "300 SMS"),
            priceSyp = 3500,
            validity = BiText("30 يوماً", "30 Days"),
            highlightTag = BiText("شاملة", "All Networks"),
            smsBonus = 300
        ),
        OfferItem(
            id = "off_sms_2",
            category = OfferCategory.SMS,
            name = BiText("باقة 100 رسالة أسبوعية", "100 Weekly SMS Pack"),
            description = BiText(
                "باقة رسائل سريعة واقتصادية للتواصل طوال الأسبوع.",
                "Fast and affordable SMS package to stay connected all week."
            ),
            quotaLabel = BiText("100 رسالة", "100 SMS"),
            priceSyp = 1500,
            validity = BiText("7 أيام", "7 Days"),
            highlightTag = BiText("سريعة", "Quick Pack"),
            smsBonus = 100
        ),

        // 3. MINUTES
        OfferItem(
            id = "off_min_1",
            category = OfferCategory.MINUTES,
            name = BiText("باقة 400 دقيقة سيريتل", "400 SyriaTel Minutes"),
            description = BiText(
                "دقائق اتصال واضحة ومباشرة لجميع خطوط سيريتل في كافة المحافظات.",
                "Crystal-clear voice minutes to all SyriaTel numbers nationwide."
            ),
            quotaLabel = BiText("400 دقيقة", "400 Minutes"),
            priceSyp = 9500,
            validity = BiText("30 يوماً", "30 Days"),
            highlightTag = BiText("قيمة ممتازة", "Best Value"),
            minutesBonus = 400
        ),
        OfferItem(
            id = "off_min_2",
            category = OfferCategory.MINUTES,
            name = BiText("باقة 150 دقيقة أسبوعية", "150 Weekly Minutes"),
            description = BiText(
                "تواصل مع عائلتك وأصدقائك طوال الأسبوع بتكلفة منخفضة.",
                "Stay in touch with family and friends all week at a low cost."
            ),
            quotaLabel = BiText("150 دقيقة", "150 Minutes"),
            priceSyp = 4500,
            validity = BiText("7 أيام", "7 Days"),
            highlightTag = BiText("أسبوعية", "Weekly"),
            minutesBonus = 150
        ),

        // 4. MIX
        OfferItem(
            id = "off_mix_1",
            category = OfferCategory.MIX,
            name = BiText("باقة ميكس بلس المتكاملة", "Mix Plus All-in-One"),
            description = BiText(
                "تجمع لك الإنترنت السريع والدقائق والرسائل في اشتراك شهري واحد.",
                "Combines fast internet, voice minutes, and SMS in one monthly subscription."
            ),
            quotaLabel = BiText("8 GB + 300 دقيقة + 150 رسالة", "8 GB + 300 Min + 150 SMS"),
            priceSyp = 22000,
            validity = BiText("30 يوماً", "30 Days"),
            highlightTag = BiText("الأكثر توفيراً", "Top Saver"),
            internetBonusGb = 8f,
            minutesBonus = 300,
            smsBonus = 150
        ),
        OfferItem(
            id = "off_mix_2",
            category = OfferCategory.MIX,
            name = BiText("باقة ميكس الشبابية", "Youth Mix Bundle"),
            description = BiText(
                "مصممة خصيصاً للشباب والطلاب مع تصفح مجاني لتطبيقات التواصل.",
                "Designed for youth and students with social media data perks."
            ),
            quotaLabel = BiText("5 GB + 200 دقيقة + 100 رسالة", "5 GB + 200 Min + 100 SMS"),
            priceSyp = 14500,
            validity = BiText("30 يوماً", "30 Days"),
            highlightTag = BiText("شبابية", "Youth Special"),
            internetBonusGb = 5f,
            minutesBonus = 200,
            smsBonus = 100
        ),

        // 5. SPECIAL OFFERS
        OfferItem(
            id = "off_spc_1",
            category = OfferCategory.SPECIAL,
            name = BiText("عرض رقمك المميز VIP", "VIP Special Offer"),
            description = BiText(
                "عرض خاص مصمم لخطك بناءً على نمط استهلاكك الشهري بخصم 40%.",
                "Tailored offer crafted for your line based on your monthly usage with 40% off."
            ),
            quotaLabel = BiText("15 GB + 500 دقيقة", "15 GB + 500 Minutes"),
            priceSyp = 20000,
            validity = BiText("30 يوماً", "30 Days"),
            highlightTag = BiText("خصم 40%", "40% OFF"),
            internetBonusGb = 15f,
            minutesBonus = 500
        ),
        OfferItem(
            id = "off_spc_2",
            category = OfferCategory.SPECIAL,
            name = BiText("باقة نهاية الأسبوع المضاعفة", "Double Weekend Pack"),
            description = BiText(
                "احصل على ضعف كمية البيانات والدقائق خلال يومي الجمعة والسبت.",
                "Get double data and voice minutes throughout Friday and Saturday."
            ),
            quotaLabel = BiText("4 GB + 120 دقيقة", "4 GB + 120 Minutes"),
            priceSyp = 5500,
            validity = BiText("3 أيام", "3 Days"),
            highlightTag = BiText("محدود", "Limited Time"),
            internetBonusGb = 4f,
            minutesBonus = 120
        ),

        // 6. ANNIVERSARY
        OfferItem(
            id = "off_ann_1",
            category = OfferCategory.ANNIVERSARY,
            name = BiText("هدية الذكرى السنوية لخطك", "Line Anniversary Loyalty Gift"),
            description = BiText(
                "بمناسبة مرور عام جديد لك في عائلة سيريتل، استمتع بباقة الولاء المجانية التجريبية.",
                "Celebrating another year with SyriaTel! Enjoy this complimentary loyalty bundle."
            ),
            quotaLabel = BiText("5 GB + 250 دقيقة + 100 رسالة", "5 GB + 250 Min + 100 SMS"),
            priceSyp = 0,
            validity = BiText("14 يوماً", "14 Days"),
            highlightTag = BiText("هدية مجانية", "Free Gift"),
            internetBonusGb = 5f,
            minutesBonus = 250,
            smsBonus = 100
        )
    )

    val initialServices = listOf(
        // SECTION A — الخدمات الأساسية
        ServiceItem(
            id = "srv_basic_1",
            section = ServiceSectionType.BASIC,
            name = BiText("خدمة إعدادات الإنترنت", "Internet APN Settings Service"),
            description = BiText(
                "ضبط وتحديث إعدادات نقطة الوصول للإنترنت الجيل الرابع تلقائياً على جهازك.",
                "Automatically configure and update 4G LTE APN internet settings on your device."
            ),
            monthlyFee = BiText("مجاناً", "Free"),
            isActive = true,
            iconKey = "wifi"
        ),
        ServiceItem(
            id = "srv_basic_2",
            section = ServiceSectionType.BASIC,
            name = BiText("خدمة سنة صلاحية", "One-Year Line Validity"),
            description = BiText(
                "تمديد صلاحية خطك لمدة 365 يوماً إضافية لضمان بقاء رقمك نشطاً دون انقطاع.",
                "Extend your SIM line validity for 365 days to keep your number continuously active."
            ),
            monthlyFee = BiText("4,500 ل.س / سنوياً", "4,500 SYP / Year"),
            isActive = true,
            iconKey = "calendar"
        ),
        ServiceItem(
            id = "srv_basic_3",
            section = ServiceSectionType.BASIC,
            name = BiText("خدمة ستوب", "Stop Ad Messages Service"),
            description = BiText(
                "التحكم بالرسائل الإعلانية والترويجية وإيقاف استلامها في الأوقات التي تختارها.",
                "Manage and block promotional SMS broadcasts whenever you prefer."
            ),
            monthlyFee = BiText("مجاناً", "Free"),
            isActive = false,
            iconKey = "block"
        ),
        ServiceItem(
            id = "srv_basic_4",
            section = ServiceSectionType.BASIC,
            name = BiText("خدمة رنة بغنية", "Ya Hala Ringback Tone"),
            description = BiText(
                "تخصيص نغمة الرنين التي يسمعها المتصلون بك بأجمل المقاطع الموسيقية والأدعية.",
                "Customize the ringback tone callers hear with music tracks or melodies."
            ),
            monthlyFee = BiText("1,500 ل.س / شهرياً", "1,500 SYP / Month"),
            isActive = true,
            iconKey = "music"
        ),
        ServiceItem(
            id = "srv_basic_5",
            section = ServiceSectionType.BASIC,
            name = BiText("خدمة الكاشف الدائم", "Missed Call Keeper Alert"),
            description = BiText(
                "إشعارك برسالة نصية بجميع المكالمات الواردة أثناء إغلاق هاتفك أو وجودك خارج التغطية.",
                "Receive SMS notifications for all calls missed while your phone was off or unreachable."
            ),
            monthlyFee = BiText("800 ل.س / شهرياً", "800 SYP / Month"),
            isActive = true,
            iconKey = "phone_missed"
        ),

        // SECTION B — خدمات منوعة
        ServiceItem(
            id = "srv_var_1",
            section = ServiceSectionType.VARIETY,
            name = BiText("خدمة الأخبار", "Breaking News Digest"),
            description = BiText(
                "أهم الأخبار المحلية والاقتصادية والعالمية تصلك يومياً عبر رسائل موجزة.",
                "Daily local, economic, and world news summaries delivered right to your phone."
            ),
            monthlyFee = BiText("1,200 ل.س / شهرياً", "1,200 SYP / Month"),
            isActive = true,
            iconKey = "newspaper"
        ),
        ServiceItem(
            id = "srv_var_2",
            section = ServiceSectionType.VARIETY,
            name = BiText("خدمة التقنية", "Tech & Digital Pulse"),
            description = BiText(
                "آخر أخبار التكنولوجيا والهواتف الذكية وتطبيقات المستقبل والنصائح الرقمية.",
                "Latest smartphone news, digital tips, and emerging technology updates."
            ),
            monthlyFee = BiText("1,000 ل.س / شهرياً", "1,000 SYP / Month"),
            isActive = false,
            iconKey = "memory"
        ),
        ServiceItem(
            id = "srv_var_3",
            section = ServiceSectionType.VARIETY,
            name = BiText("خدمة الرياضة", "Sports & Match Live"),
            description = BiText(
                "نتائج المباريات المحلية والعالمية ومواعيد الدوريات الأوروبية لحظة بلحظة.",
                "Live scores, match schedules, and local & European football updates."
            ),
            monthlyFee = BiText("1,200 ل.س / شهرياً", "1,200 SYP / Month"),
            isActive = false,
            iconKey = "sports"
        ),
        ServiceItem(
            id = "srv_var_4",
            section = ServiceSectionType.VARIETY,
            name = BiText("خدمة إسلاميات", "Islamic Reminders & Prayer"),
            description = BiText(
                "مواقيت الصلاة اليومية والأذكار الصباحية والمسائية والحكم الدينية.",
                "Daily prayer times, morning and evening supplications, and reflections."
            ),
            monthlyFee = BiText("900 ل.س / شهرياً", "900 SYP / Month"),
            isActive = true,
            iconKey = "mosque"
        )
    )

    val dailyUsageChart = listOf(
        ChartDataPoint(BiText("04:00", "04:00"), 0.25f, 8, 2),
        ChartDataPoint(BiText("08:00", "08:00"), 0.55f, 25, 6),
        ChartDataPoint(BiText("12:00", "12:00"), 0.95f, 52, 14),
        ChartDataPoint(BiText("16:00", "16:00"), 0.80f, 44, 11),
        ChartDataPoint(BiText("20:00", "20:00"), 1.15f, 68, 19),
        ChartDataPoint(BiText("23:59", "23:59"), 0.65f, 30, 8)
    )

    val weeklyUsageChart = listOf(
        ChartDataPoint(BiText("السبت", "Sat"), 0.65f, 42, 14),
        ChartDataPoint(BiText("الأحد", "Sun"), 0.50f, 34, 10),
        ChartDataPoint(BiText("الإثنين", "Mon"), 0.58f, 38, 12),
        ChartDataPoint(BiText("الثلاثاء", "Tue"), 0.48f, 29, 9),
        ChartDataPoint(BiText("الأربعاء", "Wed"), 0.62f, 36, 11),
        ChartDataPoint(BiText("الخميس", "Thu"), 0.72f, 45, 15),
        ChartDataPoint(BiText("الجمعة", "Fri"), 0.45f, 26, 9)
    )

    val initialTransactions = listOf(
        OperationRecord(
            id = "op_101",
            referenceNumber = "SYR-OP-908412",
            title = BiText("شحن الرصيد", "Balance Recharge"),
            description = BiText("عملية شحن رصيد تجريبية عبر البطاقة الإلكترونية", "Simulated balance top-up via digital voucher"),
            category = OperationCategory.RECHARGE,
            dateTimeLabel = BiText("اليوم • 10:45 ص", "Today • 10:45 AM"),
            daysAgo = 0,
            amountLabel = BiText("+15,000 ل.س", "+15,000 SYP"),
            status = OperationStatus.SUCCESS,
            extraNotes = BiText("عملية تجريبية توضيحية — لم يتم خصم أي مبالغ حقيقية.", "Demonstration operation — no real funds processed.")
        ),
        OperationRecord(
            id = "op_102",
            referenceNumber = "SYR-OP-908389",
            title = BiText("تفعيل باقة إنترنت", "Internet Package Activation"),
            description = BiText("الاشتراك في باقة الإنترنت الشهرية 12 GB", "Subscribed to 12 GB Monthly Internet Package"),
            category = OperationCategory.PACKAGES,
            dateTimeLabel = BiText("اليوم • 09:15 ص", "Today • 09:15 AM"),
            daysAgo = 0,
            amountLabel = BiText("18,000 ل.س", "18,000 SYP"),
            status = OperationStatus.SUCCESS,
            extraNotes = BiText("تاريخ التجديد القادم: 30/12/2026 (بيانات تجريبية)", "Next renewal date: 30/12/2026 (Demo data)")
        ),
        OperationRecord(
            id = "op_103",
            referenceNumber = "SYR-OP-908120",
            title = BiText("تفعيل خدمة الكاشف الدائم", "Activate Missed Call Keeper"),
            description = BiText("تم تفعيل خدمة الكاشف الدائم بنجاح على خطك التجريبي", "Missed Call Keeper service activated on your demo line"),
            category = OperationCategory.SERVICES,
            dateTimeLabel = BiText("أمس • 06:30 م", "Yesterday • 06:30 PM"),
            daysAgo = 1,
            amountLabel = BiText("800 ل.س", "800 SYP"),
            status = OperationStatus.SUCCESS,
            extraNotes = BiText("حالة الخدمة الحالية: مفعّلة", "Current service status: Active")
        ),
        OperationRecord(
            id = "op_104",
            referenceNumber = "SYR-OP-907854",
            title = BiText("عملية سيريتل كاش التجريبية", "SyriaTel Cash Demo Request"),
            description = BiText("طلب التحقق من رصيد محفظة سيريتل كاش التجريبية", "Simulated SyriaTel Cash wallet verification request"),
            category = OperationCategory.SYRIATEL_CASH,
            dateTimeLabel = BiText("منذ 3 أيام • 02:10 م", "3 days ago • 02:10 PM"),
            daysAgo = 3,
            amountLabel = BiText("0 ل.س", "0 SYP"),
            status = OperationStatus.PROCESSING,
            extraNotes = BiText("هذه العملية محاكاة لواجهة المستخدم فقط.", "This operation is a UI/UX simulation only.")
        ),
        OperationRecord(
            id = "op_105",
            referenceNumber = "SYR-OP-907411",
            title = BiText("تفعيل باقة دقائق", "Minutes Bundle Activation"),
            description = BiText("تفعيل باقة 500 دقيقة اتصال ضمن الشبكة", "Activated 500 on-net voice minutes package"),
            category = OperationCategory.PACKAGES,
            dateTimeLabel = BiText("منذ 5 أيام • 11:20 ص", "5 days ago • 11:20 AM"),
            daysAgo = 5,
            amountLabel = BiText("9,500 ل.س", "9,500 SYP"),
            status = OperationStatus.SUCCESS,
            extraNotes = BiText("صالحة لمدة 30 يوماً من تاريخ التفعيل التجريبي", "Valid for 30 days from simulated activation")
        ),
        OperationRecord(
            id = "op_106",
            referenceNumber = "SYR-OP-906992",
            title = BiText("تعديل إعدادات الحساب", "Account Settings Update"),
            description = BiText("تفعيل تنبيهات استهلاك الباقات الذكية", "Enabled smart package usage alerts"),
            category = OperationCategory.ACCOUNT,
            dateTimeLabel = BiText("منذ 12 يوماً • 08:05 م", "12 days ago • 08:05 PM"),
            daysAgo = 12,
            amountLabel = null,
            status = OperationStatus.SUCCESS,
            extraNotes = BiText("تم حفظ التفضيلات محلياً في التطبيق", "Preferences saved locally in the app")
        ),
        OperationRecord(
            id = "op_107",
            referenceNumber = "SYR-OP-906105",
            title = BiText("إيقاف خدمة الرياضة", "Deactivate Sports Service"),
            description = BiText("تم إيقاف الاشتراك في خدمة الأخبار الرياضية", "Unsubscribed from Sports News service"),
            category = OperationCategory.SERVICES,
            dateTimeLabel = BiText("منذ 20 يوماً • 04:50 م", "20 days ago • 04:50 PM"),
            daysAgo = 20,
            amountLabel = null,
            status = OperationStatus.CANCELLED,
            extraNotes = BiText("تم الإلغاء بناءً على طلب المستخدم التجريبي", "Cancelled upon demo user request")
        )
    )

    val initialNotifications = listOf(
        AppNotification(
            id = "notif_1",
            category = NotificationCategory.OFFERS,
            title = BiText("خصم 30% على باقة سوبر نت!", "30% Off SuperNet Package!"),
            message = BiText(
                "اشترك الآن في باقة 25 GB الشهرية واحصل على سرعة مضاعفة طوال الشهر.",
                "Subscribe now to the 25 GB monthly bundle and enjoy double speed all month."
            ),
            timestampLabel = BiText("منذ 15 دقيقة", "15 mins ago"),
            isRead = false
        ),
        AppNotification(
            id = "notif_2",
            category = NotificationCategory.USAGE_ALERT,
            title = BiText("تنبيه مركز الاستهلاك الذكي", "Smart Usage Center Alert"),
            message = BiText(
                "تم استهلاك 50% من باقة الدقائق الشهرية. المتبقي حالياً 250 دقيقة.",
                "You have consumed 50% of your monthly minutes package. 250 minutes remaining."
            ),
            timestampLabel = BiText("منذ ساعتين", "2 hours ago"),
            isRead = false
        ),
        AppNotification(
            id = "notif_3",
            category = NotificationCategory.SERVICES,
            title = BiText("خدمة الكاشف الدائم مفعّلة", "Missed Call Keeper Active"),
            message = BiText(
                "خدمة الكاشف الدائم تعمل بنجاح على خطك التجريبي لإعلامك بالمكالمات الفائتة.",
                "Missed Call Keeper is active on your demo line to notify you of missed calls."
            ),
            timestampLabel = BiText("أمس", "Yesterday"),
            isRead = true
        ),
        AppNotification(
            id = "notif_4",
            category = NotificationCategory.SECURITY,
            title = BiText("تحديث إعدادات الأمان", "Security Check Completed"),
            message = BiText(
                "حسابك التجريبي محمي. يمكنك تفعيل القفل بالبصمة أو رمز PIN من إعدادات الأمان.",
                "Your demo account is protected. You can enable biometric or PIN lock in Security Settings."
            ),
            timestampLabel = BiText("منذ يومين", "2 days ago"),
            isRead = true
        ),
        AppNotification(
            id = "notif_5",
            category = NotificationCategory.NEW_DEVICE,
            title = BiText("تسجيل دخول من جهاز Android", "Login from Android Device"),
            message = BiText(
                "تم تسجيل الدخول إلى تطبيق سيريتل التجريبي بنجاح من جهازك الحالي.",
                "Successfully signed in to the SyriaTel demo application from your current device."
            ),
            timestampLabel = BiText("منذ 3 أيام", "3 days ago"),
            isRead = true
        )
    )

    fun createSecondaryDemoAccountSnapshot(
        firstName: String,
        lastName: String,
        phone: String
    ): AccountSnapshot {
        return AccountSnapshot(
            accountId = "acc_2",
            userProfile = UserProfile(
                firstName = firstName,
                lastName = lastName,
                firstNameEn = firstName,
                lastNameEn = lastName,
                phoneNumber = phone,
                avatarColorIndex = 1,
                lineType = BiText("خط لاحق الدفع (فواتير)", "Postpaid Line"),
                subscriptionType = BiText("سيريتل بريميوم بلاتينيوم", "SyriaTel Premium Platinum"),
                lineStatus = BiText("نشط (حساب تجريبي)", "Active (Demo Account)"),
                activationDate = "01/09/2024",
                expiryDate = "31/12/2027",
                customerNumber = "SYR-7731904",
                daysUsed = 797
            ),
            balanceSyp = 42500,
            balanceExpiryDate = "31/12/2027",
            internetRemainingGb = 14.5f,
            internetTotalGb = 20.0f,
            internetDailyAvgGb = 0.42f,
            internetEstRemainingDays = 21,
            minutesRemaining = 380,
            minutesTotal = 600,
            minutesDailyAvg = 22,
            smsRemaining = 160,
            smsTotal = 250,
            smsDailyAvg = 7,
            services = initialServices.mapIndexed { idx, item ->
                item.copy(isActive = idx % 2 == 0)
            },
            notifications = listOf(
                AppNotification(
                    id = "notif_acc2_1",
                    category = NotificationCategory.OFFERS,
                    title = BiText("مرحباً بك في حسابك الثاني ($phone)", "Welcome to your secondary line ($phone)"),
                    message = BiText(
                        "باقة التيربو 20 GB مفعّلة حالياً على هذا الخط مع رصيد 42,500 ل.س.",
                        "Turbo 20 GB package is active on this line with 42,500 SYP available balance."
                    ),
                    timestampLabel = BiText("اليوم", "Today"),
                    isRead = false
                ),
                AppNotification(
                    id = "notif_acc2_2",
                    category = NotificationCategory.USAGE_ALERT,
                    title = BiText("حالة استهلاك ممتازة", "Optimal Package Usage"),
                    message = BiText(
                        "يتبقى لديك 14.5 GB من باقة الإنترنت و380 دقيقة اتصال على هذا الخط.",
                        "You have 14.5 GB data and 380 voice minutes remaining on this line."
                    ),
                    timestampLabel = BiText("منذ ساعة", "1 hour ago"),
                    isRead = false
                )
            ),
            transactions = listOf(
                OperationRecord(
                    id = "op_acc2_201",
                    referenceNumber = "SYR-OP-881204",
                    title = BiText("شحن رصيد إلكتروني", "Electronic Balance Top-Up"),
                    description = BiText("شحن رصيد تجريبي للخط $phone", "Simulated balance top-up for line $phone"),
                    category = OperationCategory.RECHARGE,
                    dateTimeLabel = BiText("اليوم • 11:10 ص", "Today • 11:10 AM"),
                    daysAgo = 0,
                    amountLabel = BiText("+25,000 ل.س", "+25,000 SYP"),
                    status = OperationStatus.SUCCESS,
                    extraNotes = BiText("عملية مسجلة لحساب الخط الثاني ($phone)", "Operation logged for secondary line ($phone)")
                ),
                OperationRecord(
                    id = "op_acc2_202",
                    referenceNumber = "SYR-OP-881019",
                    title = BiText("تفعيل باقة 20 GB بلاتينيوم", "Activate 20 GB Platinum Bundle"),
                    description = BiText("اشتراك شهري في باقة الإنترنت 20 GB", "Monthly subscription to 20 GB Internet Bundle"),
                    category = OperationCategory.PACKAGES,
                    dateTimeLabel = BiText("منذ يومين • 05:20 م", "2 days ago • 05:20 PM"),
                    daysAgo = 2,
                    amountLabel = BiText("28,000 ل.س", "28,000 SYP"),
                    status = OperationStatus.SUCCESS,
                    extraNotes = BiText("بيانات خاصة بالحساب الثاني", "Isolated data for secondary account")
                )
            )
        )
    }

    val faqs = listOf(
        Pair(
            BiText("كيف يمكنني شحن رصيدي أو تفعيل باقة جديدة؟", "How can I recharge my balance or activate a new package?"),
            BiText(
                "من الصفحة الرئيسية، اضغط على 'شحن الرصيد' في الوضع السريع أو بطاقة الرصيد، أو انتقل إلى قسم 'العروض' لاختيار الباقة المناسبة وتفعيلها بلمسة واحدة.",
                "From the Home screen, tap 'Recharge Balance' in Quick Actions or the Balance Card, or visit 'Offers' to pick and activate any bundle with one tap."
            )
        ),
        Pair(
            BiText("كيف أتحقق من استهلاك الإنترنت والدقائق المتبقية؟", "How do I check my remaining internet and call minutes?"),
            BiText(
                "يمكنك مشاهدة الملخص المباشر في الشاشة الرئيسية أو الضغط على 'عرض التفاصيل' لفتح مركز الاستهلاك الذكي ومتابعة الرسوم البيانية اليومية والأسبوعية.",
                "View the live summary on the Home Dashboard or tap 'View Details' to open the Smart Usage Center with daily and weekly charts."
            )
        ),
        Pair(
            BiText("ما هو نظام إدارة الحسابات المتعددة؟", "How does Multi-Account Management work?"),
            BiText(
                "يمكنك من صفحة الإعدادات إضافة حساب ثانٍ والتبديل بين رقمين مختلفين بلمسة واحدة مع حفظ رصيد وباقات وسجل عمليات كل حساب بشكل مستقل.",
                "From Settings, you can add a second account and switch between two lines in one tap, keeping each account's balance, packages, and operations isolated."
            )
        ),
        Pair(
            BiText("هل العمليات المالية في هذا التطبيق حقيقية؟", "Are financial transactions in this app real?"),
            BiText(
                "لا، هذا التطبيق هو نموذج أولي تفاعلي (Prototype) مصمم لاستعراض تجربة المستخدم والواجهات المتقدمة، وجميع العمليات والأرصدة فيه تجريبية فقط.",
                "No, this application is an interactive UI/UX prototype. All balances, recharges, and subscriptions are strictly demonstration-only."
            )
        )
    )
}
