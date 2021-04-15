<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use Tymon\JWTAuth\Facades\JWTAuth;
use Tymon\JWTAuth\Exceptions\TokenExpiredException;
use Tymon\JWTAuth\Exceptions\TokenInvalidException;

class AuthController extends Controller
{
    public function login(Request $request)
    {
        $credentials = $request->only(['email','password']);

        if( !$token = auth()->attempt($credentials) ){
            return response()->json(['error'=> 'Bad Credentials'],401);
        }
        $user = User::where('email',$credentials['email'])->first();

        return response()->json(
            [
                'id'=>$user->id,
                'name'=>$user->name,
                'token'=>$token
            ]
        );
    }

    public function verify(Request $request)
    {
        $token = $request->only(['token']);

        try {
            $user = JWTAUth::parseToken()->authenticate();
            return response()->json(['success'=>true,'user'=>$user]);
        }catch(\Exception $e){
            if( $e instanceof TokenExpiredException){
                return response()->json(['success'=>false,'token'=>JWTAuth::parseToken()->refresh(),'status'=>'expired'],200);
            }
            if($e instanceof TokenInvalidException){
                return response()->json(['success'=>false,'message'=>'token Invalid'],401);
            }
            return response()->json(['success'=>false,'message'=>'token not found'],401);
        }
    }
}
