<?php

namespace App\Http\Controllers;

use App\Http\Requests\CreateUserRequest;
use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Hash;
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
                'token'=>$token,
                'is_admin'=>$user->is_admin
            ]
        );
    }

    public function verify(Request $request)
    {
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

    public function register(Request $request)
    {
        $input = $request->all();

        if(!isset($input['name']) || !isset($input['email']) || !isset($input['password'])){
            return response()->json(['success'=>false,'message'=>'Bad Request'],400);
        }

        if(!isset($input['is_admin'])){
            $input['is_admin'] = 0;
        }

        $user = User::where('email',$input['email'])->first();
        if($user !== null){
            return response()->json(['success'=>false,'message'=>'user exists'],409);
        }

        $user = new User();
        $user->name = $input['name'];
        $user->email = $input['email'];
        $user->password = Hash::make($input['password']);
        $user->is_admin = $input['is_admin'];
        $user->save();

        return $user;
    }
}
