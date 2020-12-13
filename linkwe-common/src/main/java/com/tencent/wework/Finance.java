package com.tencent.wework;

/* sdk��������
typedef struct Slice_t {
    char* buf;
    int len;
} Slice_t;

typedef struct MediaData {
    char* outindexbuf;
    int out_len;
    char* data;    
    int data_len;
    int is_finish;
} MediaData_t;
*/

public class Finance {
    public native static long NewSdk();

	/**
	 * ��ʼ������
	 * Returnֵ=0��ʾ��API���óɹ�
	 * 
	 * @param [in]  sdk			NewSdk���ص�sdkָ��
	 * @param [in]  corpid      ������ҵ����ҵid�����磺wwd08c8exxxx5ab44d����������ҵ΢�Ź����--�ҵ���ҵ--��ҵ��Ϣ�鿴
	 * @param [in]  secret		�������ݴ浵��Secret����������ҵ΢�Ź����--������--�������ݴ浵�鿴
	 *
	 *
	 * @return �����Ƿ��ʼ���ɹ�
	 *      0   - �ɹ�
	 *      !=0 - ʧ��
	 */
    public native static int Init(long sdk, String corpid, String secret);

	/**
	 * ��ȡ�����¼����
	 * Returnֵ=0��ʾ��API���óɹ�
	 *
	 *
	 * @param [in]  sdk				NewSdk���ص�sdkָ��
	 * @param [in]  seq				��ָ����seq��ʼ��ȡ��Ϣ��ע����Ƿ��ص���Ϣ��seq+1��ʼ���أ�seqΪ֮ǰ�ӿڷ��ص����seqֵ���״�ʹ����ʹ��seq:0
	 * @param [in]  limit			һ����ȡ����Ϣ���������ֵ1000��������1000���᷵�ش���
	 * @param [in]  proxy			ʹ�ô����������Ҫ�����������ӡ��磺socks5://10.0.0.1:8081 ���� http://10.0.0.1:8081
	 * @param [in]  passwd			�����˺����룬��Ҫ���������˺����롣�� user_name:passwd_123
	 * @param [out] chatDatas		���ر�����ȡ��Ϣ�����ݣ�slice�ṹ��.���ݰ���errcode/errmsg���Լ�ÿ����Ϣ���ݡ�


	 *
	 * @return �����Ƿ���óɹ�
	 *      0   - �ɹ�
	 *      !=0 - ʧ��
	 */
    public native static int GetChatData(long sdk, long seq, long limit, String proxy, String passwd, long timeout, long chatData);

	/**
	 * ��ȡý����Ϣ����
	 * Returnֵ=0��ʾ��API���óɹ�
	 *
	 *
	 * @param [in]  sdk				NewSdk���ص�sdkָ��
	 * @param [in]  sdkFileid		��GetChatData���ص�������Ϣ�У�ý����Ϣ������sdkfileid
	 * @param [in]  proxy			ʹ�ô����������Ҫ�����������ӡ��磺socks5://10.0.0.1:8081 ���� http://10.0.0.1:8081
	 * @param [in]  passwd			�����˺����룬��Ҫ���������˺����롣�� user_name:passwd_123
	 * @param [in]  indexbuf		ý����Ϣ��Ƭ��ȡ����Ҫ����ÿ����ȡ��������Ϣ���״β���Ҫ��д��Ĭ����ȡ512k������ÿ�ε���ֻ��Ҫ���ϴε��÷��ص�outindexbuf���뼴�ɡ�
	 * @param [out] media_data		���ر�����ȡ��ý������.MediaData�ṹ��.���ݰ���data(��������)/outindexbuf(�´�����)/is_finish(��ȡ��ɱ��)

	 *
	 * @return �����Ƿ���óɹ�
	 *      0   - �ɹ�
	 *      !=0 - ʧ��
	 */
    public native static int GetMediaData(long sdk, String indexbuf, String sdkField, String proxy, String passwd, long timeout, long mediaData);

    /**
     * @brief ��������
     * @param [in]  encrypt_key, getchatdata���ص�encrypt_key
     * @param [in]  encrypt_msg, getchatdata���ص�content
     * @param [out] msg, ���ܵ���Ϣ����
	 * @return �����Ƿ���óɹ�
	 *      0   - �ɹ�
	 *      !=0 - ʧ��
     */
    public native static int DecryptData(long sdk, String encrypt_key, String encrypt_msg, long msg);
	
    public native static void DestroySdk(long sdk);
    public native static long NewSlice();
    /**
     * @brief �ͷ�slice����NewSlice�ɶ�ʹ��
     * @return 
     */
    public native static void FreeSlice(long slice);

    /**
     * @brief ��ȡslice����
     * @return ����
     */
    public native static String GetContentFromSlice(long slice);

    /**
     * @brief ��ȡslice���ݳ���
     * @return ����
     */
    public native static int GetSliceLen(long slice);
    public native static long NewMediaData();
    public native static void FreeMediaData(long mediaData);

    /**
     * @brief ��ȡmediadata outindex
     * @return outindex
     */
    public native static String GetOutIndexBuf(long mediaData);
    /**
     * @brief ��ȡmediadata data����
     * @return data
     */
    public native static byte[] GetData(long mediaData);
    public native static int GetIndexLen(long mediaData);
    public native static int GetDataLen(long mediaData);

    /**
     * @brief �ж�mediadata�Ƿ����
     * @return 1��ɡ�0δ���
     */
    public native static int IsMediaDataFinish(long mediaData);

    static {
		if (isWindows()) {
			System.loadLibrary("libeay32");
			System.loadLibrary("libprotobuf");
			System.loadLibrary("ssleay32");
			System.loadLibrary("libcurl");
			System.loadLibrary("WeWorkFinanceSdk");
		}else {
			System.loadLibrary("libWeWorkFinanceSdk_Java");
		}

    }

	public static boolean isWindows() {
		String osName = System.getProperties().getProperty("os.name");
		System.out.println("current system is " + osName);
		return osName.toUpperCase().indexOf("WINDOWS") != -1;
	}
}