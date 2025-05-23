package data.utils

import logic.exceptions.AppException
import logic.exceptions.NetworkErrorException
import logic.exceptions.UnknownDataBaseException
import java.io.IOException

abstract class BaseRepository {
    protected suspend fun <T> wrapResponse(function: suspend () -> T): T {
        return try {
            function()
        }
        catch (e: IOException) {
            throw NetworkErrorException()
        }
        catch (appException: AppException) {
            throw appException
        }
        catch (e: Exception) {
            throw UnknownDataBaseException()
        }
    }
}
