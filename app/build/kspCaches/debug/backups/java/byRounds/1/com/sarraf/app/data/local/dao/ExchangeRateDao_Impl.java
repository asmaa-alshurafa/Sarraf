package com.sarraf.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sarraf.app.data.local.entity.ExchangeRateEntity;
import com.sarraf.app.data.local.entity.RateAlertEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ExchangeRateDao_Impl implements ExchangeRateDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExchangeRateEntity> __insertionAdapterOfExchangeRateEntity;

  private final EntityInsertionAdapter<RateAlertEntity> __insertionAdapterOfRateAlertEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRatesForBase;

  private final SharedSQLiteStatement __preparedStmtOfClearAllRates;

  private final SharedSQLiteStatement __preparedStmtOfUpdateAlertStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAlert;

  private final SharedSQLiteStatement __preparedStmtOfClearAllAlerts;

  public ExchangeRateDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExchangeRateEntity = new EntityInsertionAdapter<ExchangeRateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exchange_rates` (`id`,`baseCurrency`,`currencyCode`,`currencyName`,`currencySymbol`,`flag`,`rate`,`change24h`,`sparklineJson`,`category`,`subtitle`,`timestamp`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExchangeRateEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getBaseCurrency());
        statement.bindString(3, entity.getCurrencyCode());
        statement.bindString(4, entity.getCurrencyName());
        statement.bindString(5, entity.getCurrencySymbol());
        statement.bindString(6, entity.getFlag());
        statement.bindDouble(7, entity.getRate());
        statement.bindDouble(8, entity.getChange24h());
        statement.bindString(9, entity.getSparklineJson());
        statement.bindString(10, entity.getCategory());
        statement.bindString(11, entity.getSubtitle());
        statement.bindLong(12, entity.getTimestamp());
      }
    };
    this.__insertionAdapterOfRateAlertEntity = new EntityInsertionAdapter<RateAlertEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `rate_alerts` (`id`,`baseCode`,`targetCode`,`targetRate`,`currentRate`,`conditionType`,`isEnabled`,`triggerFrequency`,`deliveryChannels`,`lastTriggered`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RateAlertEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getBaseCode());
        statement.bindString(3, entity.getTargetCode());
        statement.bindDouble(4, entity.getTargetRate());
        statement.bindDouble(5, entity.getCurrentRate());
        statement.bindString(6, entity.getConditionType());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindString(8, entity.getTriggerFrequency());
        statement.bindString(9, entity.getDeliveryChannels());
        if (entity.getLastTriggered() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getLastTriggered());
        }
      }
    };
    this.__preparedStmtOfDeleteRatesForBase = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exchange_rates WHERE baseCurrency = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllRates = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exchange_rates";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateAlertStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE rate_alerts SET isEnabled = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAlert = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM rate_alerts WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllAlerts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM rate_alerts";
        return _query;
      }
    };
  }

  @Override
  public Object insertRates(final List<ExchangeRateEntity> rates,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExchangeRateEntity.insert(rates);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAlert(final RateAlertEntity alert,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRateAlertEntity.insert(alert);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRatesForBase(final String baseCurrency,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRatesForBase.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, baseCurrency);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteRatesForBase.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllRates(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllRates.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAllRates.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateAlertStatus(final String alertId, final boolean isEnabled,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateAlertStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isEnabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, alertId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateAlertStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAlert(final String alertId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAlert.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, alertId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAlert.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllAlerts(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllAlerts.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAllAlerts.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ExchangeRateEntity>> getRatesByBaseFlow(final String baseCurrency) {
    final String _sql = "SELECT * FROM exchange_rates WHERE baseCurrency = ? ORDER BY CASE category WHEN 'PRIMARY' THEN 1 WHEN 'REGIONAL' THEN 2 ELSE 3 END, currencyCode ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, baseCurrency);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exchange_rates"}, new Callable<List<ExchangeRateEntity>>() {
      @Override
      @NonNull
      public List<ExchangeRateEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBaseCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "baseCurrency");
          final int _cursorIndexOfCurrencyCode = CursorUtil.getColumnIndexOrThrow(_cursor, "currencyCode");
          final int _cursorIndexOfCurrencyName = CursorUtil.getColumnIndexOrThrow(_cursor, "currencyName");
          final int _cursorIndexOfCurrencySymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "currencySymbol");
          final int _cursorIndexOfFlag = CursorUtil.getColumnIndexOrThrow(_cursor, "flag");
          final int _cursorIndexOfRate = CursorUtil.getColumnIndexOrThrow(_cursor, "rate");
          final int _cursorIndexOfChange24h = CursorUtil.getColumnIndexOrThrow(_cursor, "change24h");
          final int _cursorIndexOfSparklineJson = CursorUtil.getColumnIndexOrThrow(_cursor, "sparklineJson");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSubtitle = CursorUtil.getColumnIndexOrThrow(_cursor, "subtitle");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<ExchangeRateEntity> _result = new ArrayList<ExchangeRateEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExchangeRateEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpBaseCurrency;
            _tmpBaseCurrency = _cursor.getString(_cursorIndexOfBaseCurrency);
            final String _tmpCurrencyCode;
            _tmpCurrencyCode = _cursor.getString(_cursorIndexOfCurrencyCode);
            final String _tmpCurrencyName;
            _tmpCurrencyName = _cursor.getString(_cursorIndexOfCurrencyName);
            final String _tmpCurrencySymbol;
            _tmpCurrencySymbol = _cursor.getString(_cursorIndexOfCurrencySymbol);
            final String _tmpFlag;
            _tmpFlag = _cursor.getString(_cursorIndexOfFlag);
            final double _tmpRate;
            _tmpRate = _cursor.getDouble(_cursorIndexOfRate);
            final double _tmpChange24h;
            _tmpChange24h = _cursor.getDouble(_cursorIndexOfChange24h);
            final String _tmpSparklineJson;
            _tmpSparklineJson = _cursor.getString(_cursorIndexOfSparklineJson);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpSubtitle;
            _tmpSubtitle = _cursor.getString(_cursorIndexOfSubtitle);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new ExchangeRateEntity(_tmpId,_tmpBaseCurrency,_tmpCurrencyCode,_tmpCurrencyName,_tmpCurrencySymbol,_tmpFlag,_tmpRate,_tmpChange24h,_tmpSparklineJson,_tmpCategory,_tmpSubtitle,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> getTotalCachedCountFlow() {
    final String _sql = "SELECT COUNT(*) FROM exchange_rates";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exchange_rates"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<RateAlertEntity>> getAllAlertsFlow() {
    final String _sql = "SELECT * FROM rate_alerts ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rate_alerts"}, new Callable<List<RateAlertEntity>>() {
      @Override
      @NonNull
      public List<RateAlertEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBaseCode = CursorUtil.getColumnIndexOrThrow(_cursor, "baseCode");
          final int _cursorIndexOfTargetCode = CursorUtil.getColumnIndexOrThrow(_cursor, "targetCode");
          final int _cursorIndexOfTargetRate = CursorUtil.getColumnIndexOrThrow(_cursor, "targetRate");
          final int _cursorIndexOfCurrentRate = CursorUtil.getColumnIndexOrThrow(_cursor, "currentRate");
          final int _cursorIndexOfConditionType = CursorUtil.getColumnIndexOrThrow(_cursor, "conditionType");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfTriggerFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "triggerFrequency");
          final int _cursorIndexOfDeliveryChannels = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryChannels");
          final int _cursorIndexOfLastTriggered = CursorUtil.getColumnIndexOrThrow(_cursor, "lastTriggered");
          final List<RateAlertEntity> _result = new ArrayList<RateAlertEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RateAlertEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpBaseCode;
            _tmpBaseCode = _cursor.getString(_cursorIndexOfBaseCode);
            final String _tmpTargetCode;
            _tmpTargetCode = _cursor.getString(_cursorIndexOfTargetCode);
            final double _tmpTargetRate;
            _tmpTargetRate = _cursor.getDouble(_cursorIndexOfTargetRate);
            final double _tmpCurrentRate;
            _tmpCurrentRate = _cursor.getDouble(_cursorIndexOfCurrentRate);
            final String _tmpConditionType;
            _tmpConditionType = _cursor.getString(_cursorIndexOfConditionType);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final String _tmpTriggerFrequency;
            _tmpTriggerFrequency = _cursor.getString(_cursorIndexOfTriggerFrequency);
            final String _tmpDeliveryChannels;
            _tmpDeliveryChannels = _cursor.getString(_cursorIndexOfDeliveryChannels);
            final Long _tmpLastTriggered;
            if (_cursor.isNull(_cursorIndexOfLastTriggered)) {
              _tmpLastTriggered = null;
            } else {
              _tmpLastTriggered = _cursor.getLong(_cursorIndexOfLastTriggered);
            }
            _item = new RateAlertEntity(_tmpId,_tmpBaseCode,_tmpTargetCode,_tmpTargetRate,_tmpCurrentRate,_tmpConditionType,_tmpIsEnabled,_tmpTriggerFrequency,_tmpDeliveryChannels,_tmpLastTriggered);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
