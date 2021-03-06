package org.yong.mall.cache;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.yong.mall.utils.ProtoStuffSerializeUtil;

@Component
public class RedisCache {

	public final static String CACHENAME = "mall";
	public final static int CACHETIME = 60;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public <T> boolean putCache(String key, T obj) {
	    final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializeUtil.serialize(obj);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(bkey, bvalue);
			}
		});
		return result;
	}

	public <T> void putCacheWithExpireTime(String key, T obj, final long expireTime) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializeUtil.serialize(obj);
		redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setEx(bkey, expireTime, bvalue);
				return null;
			}
		});
	}

	public <T> boolean putListCache(String key, List<T> objList) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializeUtil.serillizeList(objList);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setNX(bkey, bvalue);
				return true;
			}
		});
		return result;
	}

	public <T> boolean putListCacheWithExpireTime(String key, List<T> objList, final long expireTime) {
		final byte[] bkey = key.getBytes();
		final byte[] bvalue = ProtoStuffSerializeUtil.serillizeList(objList);
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setEx(bkey, expireTime, bvalue);
				return true;
			}
		});
		return result;
	}

	public <T> T getCache(final String key, Class<T> targetClass) {
		byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.get(key.getBytes());
			}
		});
		if (result == null) {
			return null;
		}
		return ProtoStuffSerializeUtil.deserialize(result, targetClass);
	}

	public <T> List<T> getListCache(final String key, Class<T> targetClass) {
		byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
			@Override
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.get(key.getBytes());
			}
		});
		if (result == null) {
			return null;
		}
		return ProtoStuffSerializeUtil.deserializeList(result, targetClass);
	}

	public void deleteCache(String key) {
		redisTemplate.delete(key);
	}

	public void deleteCacheWithPattern(String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		redisTemplate.delete(keys);
	}

	public void clearCache() {
		deleteCacheWithPattern(RedisCache.CACHENAME + "|*");
	}
}
