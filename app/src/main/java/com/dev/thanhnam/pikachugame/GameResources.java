package com.dev.thanhnam.pikachugame;

public class GameResources {
    public static boolean CHECK = true;
    public final static int CHANGE_BACKGROUND_NOTOUCH = 0;
    public final static int CHANGE_BACKGROUND_ONTOUCH = 1;
    public final static int CHANGE_HELP_IMAGE = 2;
    public static final String VISIBLY_CONTINUE = "VISIBLY_CONTINUE";
    public static final int SET_BACKGROUND_POKEMON = 9675657;
    public static int indexBackGroundNoTouch = 5;
    public static int indexBackGroundOnTouch = 7;
    public static int indexImageHelp = 2;

    public static int[] backGroundPokemonID1 = new int[]{R.drawable.b10, R.drawable.b11, R.drawable.b12, R.drawable.b13, R.drawable.b14};//, R.drawable.b15, R.drawable.b16};
    public static int[] backGroundPokemonID2 = new int[]{R.drawable.b20, R.drawable.b21, R.drawable.b22, R.drawable.b23, R.drawable.b24};//, R.drawable.b25, R.drawable.b26};
    public static int backGroundGameID[] = new int[]{R.drawable.back1, R.drawable.back2, R.drawable.back3, R.drawable.back4, R.drawable.back5, R.drawable.back6, R.drawable.back7, R.drawable.back8, R.drawable.back9, R.drawable.back10
            , R.drawable.back11, R.drawable.back12, R.drawable.back13, R.drawable.back14, R.drawable.back15, R.drawable.back16, R.drawable.back17, R.drawable.back18, R.drawable.back19, R.drawable.back20
            , R.drawable.back21, R.drawable.back22, R.drawable.back23, R.drawable.back24, R.drawable.back25, R.drawable.back26, R.drawable.back27, R.drawable.back28, R.drawable.back29, R.drawable.back30
            , R.drawable.back31, R.drawable.back32, R.drawable.back33, R.drawable.back34, R.drawable.back35, R.drawable.back36};
    public static int[] pokemonImageID = new int[]{R.drawable.p1,
            R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5,
            R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9,
            R.drawable.p10, R.drawable.p11, R.drawable.p12, R.drawable.p13,
            R.drawable.p14, R.drawable.p15, R.drawable.p16, R.drawable.p17,
            R.drawable.p18, R.drawable.p19, R.drawable.p20, R.drawable.p21,
            R.drawable.p22, R.drawable.p23, R.drawable.p24, R.drawable.p25,
            R.drawable.p26, R.drawable.p27, R.drawable.p28, R.drawable.p29,
            R.drawable.p30, R.drawable.p31, R.drawable.p32, R.drawable.p33,
            R.drawable.p34, R.drawable.p35, R.drawable.p36, R.drawable.p37,
            R.drawable.p38, R.drawable.p39, R.drawable.p40, R.drawable.p41,
            R.drawable.p42, R.drawable.p43, R.drawable.p44, R.drawable.p45,
            R.drawable.p46, R.drawable.p47, R.drawable.p48, R.drawable.p49,
            R.drawable.p50, R.drawable.p51, R.drawable.p52, R.drawable.p53,
            R.drawable.p54, R.drawable.p55, R.drawable.p56, R.drawable.p57,
            R.drawable.p58, R.drawable.p59, R.drawable.p60, R.drawable.p61,
            R.drawable.p62, R.drawable.p63, R.drawable.p64, R.drawable.p65,
            R.drawable.p66, R.drawable.p67, R.drawable.p68, R.drawable.p69,
            R.drawable.p70, R.drawable.p71, R.drawable.p72, R.drawable.p73,
            R.drawable.p74, R.drawable.p75, R.drawable.p76, R.drawable.p77,
            R.drawable.p78, R.drawable.p79, R.drawable.p80, R.drawable.p81,
            R.drawable.p82, R.drawable.p83, R.drawable.p84, R.drawable.p85,
            R.drawable.p86, R.drawable.p87, R.drawable.p88, R.drawable.p89,
            R.drawable.p90, R.drawable.p91, R.drawable.p92, R.drawable.p93,
            R.drawable.p94, R.drawable.p95, R.drawable.p96, R.drawable.p97,
            R.drawable.p98, R.drawable.p99, R.drawable.p100, R.drawable.p101,
            R.drawable.p102, R.drawable.p103, R.drawable.p104, R.drawable.p105,
            R.drawable.p106, R.drawable.p107, R.drawable.p108, R.drawable.p109,
            R.drawable.p110, R.drawable.p111, R.drawable.p112, R.drawable.p113,
            R.drawable.p114, R.drawable.p115, R.drawable.p116, R.drawable.p117,
            R.drawable.p118, R.drawable.p119, R.drawable.p120, R.drawable.p121,
            R.drawable.p122, R.drawable.p123, R.drawable.p124, R.drawable.p125,
            R.drawable.p126, R.drawable.p127, R.drawable.p128, R.drawable.p129,
            R.drawable.p130, R.drawable.p131, R.drawable.p132, R.drawable.p133,
            R.drawable.p134, R.drawable.p135, R.drawable.p136, R.drawable.p137,
            R.drawable.p138, R.drawable.p139, R.drawable.p140, R.drawable.p141,
            R.drawable.p142, R.drawable.p143, R.drawable.p144, R.drawable.p145,
            R.drawable.p146, R.drawable.p147, R.drawable.p148, R.drawable.p149,
            R.drawable.p150, R.drawable.p151, R.drawable.p152, R.drawable.p153,
            R.drawable.p154, R.drawable.p155, R.drawable.p156, R.drawable.p157,
            R.drawable.p158, R.drawable.p159, R.drawable.p160, R.drawable.p161,
            R.drawable.p162, R.drawable.p163, R.drawable.p164, R.drawable.p165,
            R.drawable.p166, R.drawable.p167, R.drawable.p168, R.drawable.p169,
            R.drawable.p170, R.drawable.p171, R.drawable.p172, R.drawable.p173,
            R.drawable.p174, R.drawable.p175, R.drawable.p176, R.drawable.p177,
            R.drawable.p178, R.drawable.p179, R.drawable.p180, R.drawable.p181,
            R.drawable.p182, R.drawable.p183, R.drawable.p184, R.drawable.p185,
            R.drawable.p186, R.drawable.p187, R.drawable.p188, R.drawable.p189,
            R.drawable.p190, R.drawable.p191, R.drawable.p192, R.drawable.p193,
            R.drawable.p194, R.drawable.p195, R.drawable.p196, R.drawable.p197,
            R.drawable.p198, R.drawable.p199, R.drawable.p200, R.drawable.p201,
            R.drawable.p202, R.drawable.p203, R.drawable.p204, R.drawable.p205,
            R.drawable.p206, R.drawable.p207, R.drawable.p208, R.drawable.p209,
            R.drawable.p210, R.drawable.p211, R.drawable.p212, R.drawable.p213,
            R.drawable.p214, R.drawable.p215, R.drawable.p216, R.drawable.p217,
            R.drawable.p218, R.drawable.p219, R.drawable.p220, R.drawable.p221,
            R.drawable.p222, R.drawable.p223, R.drawable.p224, R.drawable.p225,
            R.drawable.p226, R.drawable.p227, R.drawable.p228, R.drawable.p229,
            R.drawable.p230, R.drawable.p231, R.drawable.p232, R.drawable.p233,
            R.drawable.p234, R.drawable.p235, R.drawable.p236, R.drawable.p237,
            R.drawable.p238, R.drawable.p239, R.drawable.p240, R.drawable.p241,
            R.drawable.p242, R.drawable.p243, R.drawable.p244, R.drawable.p245,
            R.drawable.p246, R.drawable.p247, R.drawable.p248, R.drawable.p249,
            R.drawable.p250, R.drawable.p251, R.drawable.p252, R.drawable.p253,
            R.drawable.p254, R.drawable.p255, R.drawable.p256, R.drawable.p257,
            R.drawable.p258, R.drawable.p259, R.drawable.p260, R.drawable.p261,
            R.drawable.p262, R.drawable.p263, R.drawable.p264, R.drawable.p265,
            R.drawable.p266, R.drawable.p267, R.drawable.p268, R.drawable.p269,
            R.drawable.p270, R.drawable.p271, R.drawable.p272, R.drawable.p273,
            R.drawable.p274, R.drawable.p275, R.drawable.p276, R.drawable.p277,
            R.drawable.p278, R.drawable.p279, R.drawable.p280, R.drawable.p281,
            R.drawable.p282, R.drawable.p283, R.drawable.p284, R.drawable.p285,
            R.drawable.p286, R.drawable.p287, R.drawable.p288, R.drawable.p289,
            R.drawable.p290, R.drawable.p291, R.drawable.p292, R.drawable.p293,
            R.drawable.p294, R.drawable.p295, R.drawable.p296, R.drawable.p297,
            R.drawable.p298, R.drawable.p299, R.drawable.p300, R.drawable.p301,
            R.drawable.p302, R.drawable.p303, R.drawable.p304, R.drawable.p305,
            R.drawable.p306, R.drawable.p307, R.drawable.p308, R.drawable.p309,
            R.drawable.p310, R.drawable.p311, R.drawable.p312, R.drawable.p313,
            R.drawable.p314, R.drawable.p315, R.drawable.p316, R.drawable.p317,
            R.drawable.p318};
    public static int helpImageID = R.drawable.help8;
}
