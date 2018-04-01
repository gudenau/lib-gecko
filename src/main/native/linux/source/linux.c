#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <termios.h>
#include <zlib.h>

#include <jni.h>

#include <net_gudenau_jgecko_natives_Linux.h>

#define UNUSED_PARAM(param) {param = param;}

/*
 * Class:     net_gudenau_jgecko_natives_Linux
 * Method:    doGetRealFD
 * Signature: (Ljava/io/FileDescriptor;)I
 */
JNIEXPORT jint JNICALL Java_net_gudenau_jgecko_natives_Linux_doGetRealFD
	(JNIEnv* env, jclass klass, jobject descriptor){
	UNUSED_PARAM(klass);

	jclass FileDescriptor = (*env)->FindClass(
		env,
		"java/io/FileDescriptor"
	);
	if(!FileDescriptor){
		return 0;
	}

	jfieldID fd = (*env)->GetFieldID(
		env,
		FileDescriptor,
		"fd",
		"I"
	);
	if(!fd){
		return 0;
	}

	return (*env)->GetIntField(
		env,
		descriptor,
		fd
	);
}

/*
 * Class:     net_gudenau_jgecko_natives_Linux
 * Method:    getBufferAddress
 * Signature: (Ljava/nio/ByteBuffer;)J
 */
JNIEXPORT jlong JNICALL Java_net_gudenau_jgecko_natives_Linux_getBufferAddress
	(JNIEnv* env, jclass klass, jobject buffer){
	UNUSED_PARAM(klass);
	return (jlong)(*env)->GetDirectBufferAddress(env, buffer);
}

/*
 * Class:     net_gudenau_jgecko_natives_Linux
 * Method:    getTermiosSize
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_net_gudenau_jgecko_natives_Linux_getTermiosSize
	(JNIEnv* env, jclass klass){
	UNUSED_PARAM(env);
	UNUSED_PARAM(klass);
	return (jint)sizeof(struct termios);
}

/*
 * Class:     net_gudenau_jgecko_natives_Linux
 * Method:    fcntl
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_net_gudenau_jgecko_natives_Linux_fcntl
	(JNIEnv* env, jclass klass, jint fd, jint cmd, jint arg){
	UNUSED_PARAM(env);
	UNUSED_PARAM(klass);

	return (jint)fcntl((int)fd, (int)cmd, (int)arg);
}

/*
 * Class:     net_gudenau_jgecko_natives_Linux
 * Method:    tcgetattr
 * Signature: (IJ)I
 */
JNIEXPORT jint JNICALL Java_net_gudenau_jgecko_natives_Linux_tcgetattr
	(JNIEnv* env, jclass klass, jint fd, jlong termios_p){
	UNUSED_PARAM(env);
    UNUSED_PARAM(klass);

    return (jint)tcgetattr((int)fd, (struct termios*)(termios_p));
}

/*
 * Class:     net_gudenau_jgecko_natives_Linux
 * Method:    cfmakeraw
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_gudenau_jgecko_natives_Linux_cfmakeraw
	(JNIEnv* env, jclass klass, jlong termios_p){
	UNUSED_PARAM(env);
	UNUSED_PARAM(klass);

	cfmakeraw((struct termios*)termios_p);
}

/*
 * Class:     net_gudenau_jgecko_natives_Linux
 * Method:    tcsetattr
 * Signature: (IIJ)I
 */
JNIEXPORT jint JNICALL Java_net_gudenau_jgecko_natives_Linux_tcsetattr
	(JNIEnv* env, jclass klass, jint fd, jint optional_actions, jlong termios_p){
	UNUSED_PARAM(env);
	UNUSED_PARAM(klass);

	return (jint)tcsetattr((int)fd, (int)optional_actions, (struct termios*)termios_p);
}

/*
 * Class:     net_gudenau_jgecko_natives_Linux
 * Method:    tcflush
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_net_gudenau_jgecko_natives_Linux_tcflush
	(JNIEnv* env, jclass klass, jint fd, jint queue_selector){
	UNUSED_PARAM(env);
	UNUSED_PARAM(klass);

	return (jint)tcflush((int)fd, (int)queue_selector);
}

/*
 * Class:     net_gudenau_jgecko_natives_Linux
 * Method:    compress2
 * Signature: (JJJJI)I
 */
JNIEXPORT jint JNICALL Java_net_gudenau_jgecko_natives_Linux_compress2
	(JNIEnv* env, jclass klass, jlong dest, jlong destSize, jlong source, jlong sourceSize, jint level){
	UNUSED_PARAM(env);
	UNUSED_PARAM(klass);

	return (jint)compress2((void*)dest, (uLongf*)destSize, (void*)source, (uLongf)sourceSize, (int)level);
}
