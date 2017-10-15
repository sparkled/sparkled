import React from 'react';
import { Link } from 'react-router-dom';

export default ({ song }) => (
  <div className="card">
    <div className="card-header d-flex justify-content-between align-items-center">
      <h4 className="mb-0">{song.name}</h4>

      <div>
        <Link className="btn btn-sm btn-info" to={`/songs/${song.id}`}>Edit</Link>
      </div>
    </div>

    <div className="card-block">
      <div className="d-flex justify-content-between">
        <h6>Album: {song.album}</h6>
        <h6 className="ml-3">{getFormattedDuration(song)}</h6>
      </div>

      <div className="d-flex justify-content-between">
        <h6>Artist: {song.artist}</h6>
        <div className="ml-3">
          <span className="badge badge-primary">{song.status}</span>
        </div>
      </div>
    </div>
  </div>
);

function getFormattedDuration(song) {
  const date = new Date(null);
  date.setSeconds(Math.round(song.durationFrames / song.framesPerSecond));

  // 1970-01-01T00:01:23.000Z
  //               ^^^^^
  return date.toISOString().substr(14, 5);
}
