<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;


Route::post('login',[AuthController::class,'login']);
Route::post('verify',[AuthController::class,'verify']);

Route::group(['middleware' => ['jwtauth']], function() {
    Route::post('register',[AuthController::class,'register']);
});


