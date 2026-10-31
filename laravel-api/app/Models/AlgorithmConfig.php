<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class AlgorithmConfig extends Model
{
    use HasFactory;

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'adjustment_factor',
        'clamp_min',
        'clamp_max',
        'cutoff_score',
        'list_size',
        'era_weight',
        'director_weight',
        'genre_weight',
        'duration_weight',
    ];

    /**
     * Get the attributes that should be cast.
     *
     * @return array<string, string>
     */
    protected function casts(): array
    {
        return [
            'id' => 'integer',
            'adjustment_factor' => 'float',
            'clamp_min' => 'float',
            'clamp_max' => 'float',
            'cutoff_score' => 'float',
            'era_weight' => 'float',
            'director_weight' => 'float',
            'genre_weight' => 'float',
            'duration_weight' => 'float',
        ];
    }
}
